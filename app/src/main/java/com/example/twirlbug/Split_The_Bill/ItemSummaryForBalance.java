package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Twirlbug on 3/21/2016.
 */
public class ItemSummaryForBalance extends AppCompatActivity {

    private static final String ARG_NAME = "main_name";
    private static final String SINGLE_NAMES = "single_name";

    private String person1name;

    private int fromPurchase;
    private TextView mItemTotal;
    private ListView mParticipantList;
    private TextView mNameText;
    private Button mViewItems;
    private ArrayList<String> names;
    private String[] nameArray;
    private double[][] balances;


    public static Intent newIntent(Context packageContext, String personName){
        Intent intent = new Intent(packageContext, ItemSummaryForBalance.class);
        Bundle b = new Bundle();
        b.putString(ARG_NAME, personName);
        intent.putExtras(b);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            person1name = bundle.getString(ARG_NAME);
        } else {
            SharedPreferences settings = getSharedPreferences(SINGLE_NAMES, 0);
            person1name = settings.getString("person1", person1name);
        }


        setContentView(R.layout.item_summary_for_balance);

        ItemLister itemLister = ItemLister.get(this);
        List<Item> list_of_items = itemLister.getItems(person1name, getBaseContext());

        mNameText = (TextView) findViewById(R.id.single_name);
        String name = getString(R.string.name_single, person1name);
        mNameText.setText(name);

        int itemCount = 0;
        itemCount= itemLister.getItems(person1name, getBaseContext()).size();
        String itemTotal = getString(R.string.total_items, itemCount);
        mItemTotal = (TextView) findViewById(R.id.total_items);
        mItemTotal.setText(itemTotal);



        //Query the databse for names related to this one
        DatabaseHelper dbh = new DatabaseHelper(getBaseContext());
        names = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT * "
                + " FROM " + DbSchema.TableInfo.Item_Table
                + " WHERE " + DbSchema.TableInfo.Itemized_Purchase.IC + " = ?"
                + " OR " + DbSchema.TableInfo.Itemized_Purchase.IB + " = ?";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{person1name, person1name});
        names.add("none");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //get buyer name, if not in array list then add it
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(DbSchema.TableInfo.Itemized_Purchase.IB))  &&
                        !cursor.getString(cursor.getColumnIndexOrThrow(DbSchema.TableInfo.Itemized_Purchase.IB)).equals("") ) {
                    String buyer = cursor.getString(cursor.getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IB));
                    if (!names.contains(buyer)) {
                        names.add(buyer);
                    }
                }
                //get consumer name, if not in array list then add it
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(DbSchema.TableInfo.Itemized_Purchase.IC))  &&
                        !cursor.getString(cursor.getColumnIndexOrThrow(DbSchema.TableInfo.Itemized_Purchase.IC)).equals("") ) {
                    String consumer = cursor.getString(cursor.getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IC));
                    if (!names.contains(consumer)) {
                        names.add(consumer);
                    }
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
        if (names.size()>0) names.remove("none");
        if (names.contains(person1name)) names.remove(person1name); // cannot have a balance to yourself
        Collections.sort(names);
        nameArray = names.toArray(new String[0]);



        //set up for each name have an equavalent space for dollars and cents and for if they are buyer or consumer
        balances = new double[names.size()][2]; // 0 for person in question 1 for others

        for (Item item : list_of_items){
            if(!item.getmGifted()) {
                String StringDollars = Double.toString(item.getmDollars());
                String[] StringArrayDollars = StringDollars.split("\\.");
                String StringCents = Double.toString(item.getmCents());
                String[] StringArrayCents = StringCents.split("\\.");
                String Stringmoney = StringArrayDollars[0] + "." + StringArrayCents[0];
                double money = Double.parseDouble(Stringmoney);
                //Toast.makeText(getBaseContext(), "CHECKING ITEMS " + money, Toast.LENGTH_SHORT).show();
                if (item.getmBuyer().equals(person1name)) {
                    //Toast.makeText(getBaseContext(), "Buyer True", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < nameArray.length; i++) {
                        if (nameArray[i].equals(item.getmConsumer())) {
                            balances[i][0] += money;
                        }
                    }

                } else if (item.getmConsumer().equals(person1name)) {
                    //Toast.makeText(getBaseContext(), "Consumer True", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < nameArray.length; i++) {
                        if (nameArray[i].equals(item.getmBuyer())) {
                            balances[i][1] += money;
                        }
                    }

                }
            }

        }

        double[] currentbalances = new double[nameArray.length];
        final String [] namesBalances= new String[nameArray.length];
        for (int i = 0; i <nameArray.length; i++){ //fill nameBalances
            double amountOwed = balances[i][0] - balances[i][1];
            NumberFormat formatter = new DecimalFormat("#0.00");
            String formated1Price = formatter.format(amountOwed);
            String formated1PriceNegative = formatter.format(-amountOwed);
            if (amountOwed == 0){
                namesBalances[i]= nameArray[i] + " is even.";
            } else if (amountOwed > 0){
                namesBalances[i] = nameArray[i] + " owes $" + formated1Price + ".";
            }else if (amountOwed < 0){
                namesBalances[i] = nameArray[i] + " is owed $" + formated1PriceNegative  + ".";
            }
        }

        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, namesBalances) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                double amountOwed = balances[position][0] - balances[position][1];
                if (amountOwed == 0.0) {
                    text.setTextColor(getResources().getColor(R.color.Black));
                    view.setBackgroundColor(getResources().getColor(R.color.White));
                }else if (amountOwed > 0.0) {
                        //text.setTextColor(getResources().getColor(R.color.Forest_Green));
                        view.setBackgroundColor(getResources().getColor(R.color.Mint));
                } else if (amountOwed < 0.0) {
                    //text.setTextColor(getResources().getColor(R.color.Red));
                    view.setBackgroundColor(getResources().getColor(R.color.Light_Red));}
                //text.setText(Double.toString(amountOwed));
                return view;
            }
        };

        mParticipantList= (ListView) findViewById(R.id.participant_list);
        mParticipantList.setAdapter(arrayAdapter);

        mViewItems = (Button) findViewById(R.id.View_single_button);
        mViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ItemListBalanceActivity.class);
                Bundle b = new Bundle();
                b.putInt("from_purchase", 2);
                b.putString("person_1", person1name);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public  void onPause(){
        super.onPause();
        SharedPreferences settings = getSharedPreferences(SINGLE_NAMES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("person1", person1name);
        editor.commit();
    }
}





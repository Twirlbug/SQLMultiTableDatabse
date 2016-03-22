package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Twirlbug on 3/20/2016.
 */
public class ItemSummary extends Activity{

    private static String ARG_FROM_PURCHASE = "from_purchase";
    private static String ARG_PURCHASE_ID = "purchase_id";
    private int fromPurchase;
    private int purchaseId;

    private TextView mItemTotal;
    private TextView mPriceTotal;
    private ListView mParticipantList;

    private  ArrayList<String> names;



    public static Intent newIntent(Context packageContext, int fromPurchase, int purchaseId){
        Intent intent = new Intent(packageContext, ItemSummary.class);
        Bundle b = new Bundle();
        b.putInt(ARG_FROM_PURCHASE, fromPurchase);
        b.putInt(ARG_PURCHASE_ID, purchaseId);
        intent.putExtras(b);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        fromPurchase = bundle.getInt(ARG_FROM_PURCHASE);
        purchaseId = bundle.getInt(ARG_PURCHASE_ID);

        setContentView(R.layout.item_summary);


        ItemLister itemLister = ItemLister.get(this);
        List<Item> list_of_items = itemLister.getItems(purchaseId);


        int itemCount = 0;
        itemCount= itemLister.getItems(purchaseId).size();
        String itemTotal = getString(R.string.total_items, itemCount);

        mItemTotal = (TextView) findViewById(R.id.total_items);
        mItemTotal.setText(itemTotal);


        double itemDollarPrice = 0.0;
        double itemCentsPrice = 0.0;
        for (Item item : list_of_items) {
            itemDollarPrice += item.getmDollars();
            itemCentsPrice = item.getmCents();
        }

        Log.d("Price setting ", "Price is " + itemDollarPrice + " and " + itemCentsPrice/100 +" cents");
        double itemTotalPrice = itemDollarPrice +(itemCentsPrice/100);
        NumberFormat formatter = new DecimalFormat("#0.00");
        String formatedPrice = formatter.format(itemTotalPrice);
        String priceTotal = getString(R.string.total_price, formatedPrice);
        mPriceTotal = (TextView) findViewById(R.id.items_price);
        mPriceTotal.setText(priceTotal);

        mParticipantList = (ListView) findViewById(R.id.participant_list);
        DatabaseHelper dbh = new DatabaseHelper(getBaseContext());
        names = new ArrayList<>();

        //query the database for the list of names
        String selectQuery = "SELECT * "
                + " FROM " + DbSchema.TableInfo.Item_Table
                + " WHERE " + DbSchema.TableInfo.Itemized_Purchase.MID + " = ?";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(purchaseId)});
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
        Collections.sort(names);
        //used for testing the ArrayAdapter
        ///ArrayList<String> test = new ArrayList<>();
        //test.add("test1");

        //Array adapter for the list view
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, names);

        mParticipantList.setAdapter(arrayAdapter);


    }

    private void GetAllNameFromPurchase(int Purchaseid){

    }

}


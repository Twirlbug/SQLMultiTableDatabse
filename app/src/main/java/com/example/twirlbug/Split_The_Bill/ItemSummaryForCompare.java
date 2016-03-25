package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Twirlbug on 3/20/2016.
 */
public class ItemSummaryForCompare extends Activity {

    private static String PERSON_1 = "person_1";
    private static String PERSON_2 = "person_2";

    private String person1name;
    private String person2name;

    private TextView mBalanceDisplay;
    private TextView mItemTotal;
    private TextView mName1Text;
    private TextView mName2Text;
    private Button mViewItems;


    public static Intent newIntent(Context packageContext, String person1, String person2) {
        Intent intent = new Intent(packageContext, ItemSummaryForCompare.class);
        Bundle b = new Bundle();
        b.putString(PERSON_1, person1);
        b.putString(PERSON_2, person2);
        intent.putExtras(b);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        person1name = bundle.getString(PERSON_1);
        person2name = bundle.getString(PERSON_2);

        setContentView(R.layout.item_summary_for_compare2);

        mName1Text = (TextView) findViewById(R.id.person1);
        String name = getString(R.string.name_1, person1name);
        mName1Text.setText(name);

        mName2Text = (TextView) findViewById(R.id.person2);
        String name2 = getString(R.string.name_2, person2name);
        mName2Text.setText(name2);

        mBalanceDisplay = (TextView) findViewById(R.id.compare_statement);


        ItemLister itemLister = ItemLister.get(this);
        List<Item> list_of_items = itemLister.getItems(person1name, person2name, getBaseContext());

        int itemCount = 0;
        itemCount = itemLister.getItems(person1name, person2name, getBaseContext()).size();
        String itemTotal = getString(R.string.total_items, itemCount);

        mItemTotal = (TextView) findViewById(R.id.total_items);
        mItemTotal.setText(itemTotal);

        List<Item> copy = new ArrayList<Item>(list_of_items); //copy for gift removal process

        for (Item item : copy) {
            if (item.getmGifted()) {
                list_of_items.remove(item);
            }
        }


        //how much each one owes to the other
        double person1Dollar = 0.0;
        double person1Cents = 0.0;
        double person2Dollar = 0.0;
        double person2Cents = 0.0;
        double allDollars = 0.0;
        double allCents = 0.0;
        double itemTotalPerson1 = 0.0;
        double itemTotalPerson2 = 0.0;


        for (Item item : list_of_items) {
            double dollars = item.getmDollars();
            double cents = item.getmCents();
            allDollars += dollars;
            allCents += cents;
            if (item.getmBuyer().equals(person1name)) { //if person 1 bought it it gets added to person2s tab
                person2Cents += cents;
                person2Dollar += dollars;
                //Toast.makeText(getBaseContext(), "Added to Person 2", Toast.LENGTH_SHORT).show();
            }
            if (item.getmBuyer().equals(person2name)) {
                person1Cents += cents;
                person1Dollar += dollars;
                //Toast.makeText(getBaseContext(), "Added to Person 1", Toast.LENGTH_SHORT).show();
            }


        }


        itemTotalPerson1 += person1Dollar;
        itemTotalPerson1 += (person1Cents / 100);
        itemTotalPerson2 += person2Dollar;
        itemTotalPerson2 += (person2Cents / 100);
        //Toast.makeText(getBaseContext(), "Person 1 total: " + itemTotalPerson1 + " Person 2 total: " +itemTotalPerson2, Toast.LENGTH_LONG).show();


        if (itemTotalPerson1 > itemTotalPerson2) { // this means person 1 owes person 2
            String person1Owes = person1name + " owes " + person2name;
            double person1OweBalance = itemTotalPerson1 - itemTotalPerson2;
            NumberFormat formatter = new DecimalFormat("#0.00");
            String formated1Price = formatter.format(person1OweBalance);
            person1Owes += " $" + formated1Price + ".  ";
            mBalanceDisplay.setText(person1Owes);
        } else if (itemTotalPerson1 < itemTotalPerson2) { //this means person 2 owes person 1
            String person2Owes = person2name + " owes " + person1name;
            double person2OweBalance = itemTotalPerson2 - itemTotalPerson1;
            NumberFormat formatter = new DecimalFormat("#0.00");
            String formated2Price = formatter.format(person2OweBalance);
            person2Owes += " $" + formated2Price + ".  ";
            mBalanceDisplay.setText(person2Owes);
        } else { //person 1 and person 2 are completely even
            //mBalanceDisplay.setText(person1name + " and " + person2name + " are even.");
            mBalanceDisplay.setText(person1name + " and " + person2name + " are even.");
        }

        mViewItems = (Button) findViewById(R.id.View_2_button);
        mViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ItemListComparisonActivity.class);
                Bundle b = new Bundle();
                b.putInt("from_purchase", 0);
                b.putString("person_1", person1name);
                b.putString("person_2", person2name);
                intent.putExtras(b);
                startActivity(intent);

            }
        });


    }
}

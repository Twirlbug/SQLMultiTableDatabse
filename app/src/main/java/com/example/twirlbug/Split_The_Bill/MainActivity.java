package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;

import java.util.List;

/**
 * Created by Nicole Geiger on 3/14/2016.
 * Used for testing only
 */
public class MainActivity extends Activity {

    private Button mTransaction;
    private Button mComparisons;
    private Button mBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_activity);

        mTransaction = (Button) findViewById(R.id.view_add_trans);
        mBalance = (Button) findViewById(R.id.single_person);
        mComparisons = (Button) findViewById(R.id.compare);

        mTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts transaction lister
                Intent intent = new Intent(getBaseContext(), PurchaseListActivity.class);
                startActivity(intent);
            }
        });

        mBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts transaction lister
                Intent intent = new Intent(getBaseContext(), SingleBalanceActivity.class);
                startActivity(intent);
            }
        });

        mComparisons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts transaction lister
                Intent intent = new Intent(getBaseContext(), CompareTabs.class);
                startActivity(intent);
            }
        });

    }
}


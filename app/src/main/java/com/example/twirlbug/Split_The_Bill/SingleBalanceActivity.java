package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Twirlbug on 3/21/2016.
 */
public class SingleBalanceActivity extends Activity {

    private static final int REQUEST_BUYER= 1;


    private Button mBuyer;
    private Button mSubmit;

    private boolean pickedBuyer;
    private String buyerName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_picker);
        pickedBuyer = false;


        final Intent pickBuyer = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mBuyer = (Button) findViewById(R.id.Buyer_picker);
        mBuyer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickBuyer, REQUEST_BUYER);
            }
        });


        PackageManager packageManager = this.getPackageManager();
        if(packageManager.resolveActivity(pickBuyer, PackageManager.MATCH_DEFAULT_ONLY)==null){
            mBuyer.setEnabled(false);
        }



        mSubmit = (Button) findViewById(R.id.submit_purchase);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedBuyer == false){
                    Toast.makeText(getBaseContext(), "Must Choose a Person", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getBaseContext(), ItemListBalanceActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("from_purchase", 2);
                    b.putString("person_1", buyerName);
                    intent.putExtras(b);
                    startActivity(intent);

                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_BUYER&&data !=null){
            Uri contactUri= data.getData();
            //Specify which fields you want your query to return values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query - contactUri is like a "where" clause here
            Cursor c =this.getContentResolver()
                    .query(contactUri, queryFields, null,null,null);
            try {
                //Double check that you got results
                if (c.getCount() == 0) {
                    return;
                }
                //pullout the first column of the first row of data-that is your suspect's name
                c.moveToFirst();
                String buyer = c.getString(0);
                pickedBuyer = true;
                buyerName = buyer;
                mBuyer.setText(buyer);
            }finally {
                c.close();
            }
        }

    }
}
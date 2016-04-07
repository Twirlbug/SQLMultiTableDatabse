package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.text.NumberFormat;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/30/2016.
 */
public class ItemActivity extends AppCompatActivity {

    private static final String ARG_ITEM_ID = "item_id";
    private static final String ARG_TRANS_ID = "transaction_id";

    private static final int REQUEST_BUYER= 1;
    private static final int REQUEST_RECEIVER = 2;

    private Item mItem;
    private  UUID itemId;
    private int purchasedId;
    private boolean buyer;
    private boolean receiver;


    private EditText mEnterName;
    private Button mBuyer;
    private Button mReciever;
    private EditText mDollars;
    private EditText mCents;
    private CheckBox mGifted;
    private Button mSubmitButton;
    private Button mDeleteButton;

    public static Intent newInstance(UUID itemId, int purchaseID, Context packageContext) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);
        args.putInt(ARG_TRANS_ID, purchaseID);
        Intent activity = new Intent(packageContext, ItemActivity.class);
        activity.putExtras(args);
        return activity;
    }


    @Override
    public void onPause(){
        super.onPause();
        ItemLister.get(this).updateItem(mItem);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        itemId = (UUID) bundle.getSerializable(ARG_ITEM_ID);
        mItem = ItemLister.get(this).getItem(itemId);
        purchasedId = bundle.getInt(ARG_TRANS_ID);
        if (mItem.getmTransactionID() == 0) mItem.setmTransactionID(purchasedId);
        //Toast.makeText(getContext(), " OnCreate ItemFragment", Toast.LENGTH_SHORT);

        setContentView(R.layout.fragment_item);
        receiver = false;
        buyer = false;

        //sets the name entry text and allows for user changes
        mEnterName = (EditText) findViewById(R.id.item_name);
        mEnterName.setText(mItem.getmName());
        mEnterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setmName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Intent pickBuyer = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mBuyer = (Button) findViewById(R.id.Buyer_picker);
        mBuyer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickBuyer, REQUEST_BUYER);
            }
        });

        if (mItem.getmBuyer() != null) {
            mBuyer.setText(mItem.getmBuyer());
        }
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.resolveActivity(pickBuyer, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mBuyer.setEnabled(false);
        }

        final Intent pickReciever = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mReciever = (Button) findViewById(R.id.Receiver_picker);
        mReciever.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickReciever, REQUEST_RECEIVER);
            }
        });

        if (mItem.getmBuyer() != null) {
            mReciever.setText(mItem.getmConsumer());
        }
        PackageManager packageManagerreceiver = this.getPackageManager();
        if (packageManagerreceiver.resolveActivity(pickReciever, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mReciever.setEnabled(false);
        }


        //sets the price entry text and allows for user changes, also makes sure new entry is a number
        mDollars = (EditText)findViewById(R.id.dollars);
        double dollarsString = mItem.getmDollars()*100  + mItem.getmCents();
        final String dollars = NumberFormat.getCurrencyInstance().format((dollarsString/100));
        mDollars.setText(dollars);

        mDollars.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = dollars;
                if (!s.toString().equals(current)) {
                    mDollars.removeTextChangedListener(this);


                    try {
                        String cleanString = s.toString().replaceAll("[$,.]", "");

                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                        current = formatted;
                        String cleanup = current.replaceAll("[$]", "");
                        String[] splitfromDatabse = cleanup.split("\\.");
                        mItem.setmDollars(Integer.parseInt(splitfromDatabse[0]));
                        mItem.setmCents(Integer.parseInt(splitfromDatabse[1]));
                        mDollars.setText(formatted);
                        mDollars.setSelection(formatted.length());
                        mDollars.addTextChangedListener(this);
                    } catch (NumberFormatException exc) {
                        Toast.makeText(getBaseContext(), "Price must be a number.", Toast.LENGTH_SHORT).show();
                        mDollars.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //sets the gifted check box and allows for updates
        mGifted = (CheckBox) findViewById(R.id.gifted_checkbox);
        mGifted.setChecked(mItem.getmGifted());
        mGifted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set the item's gifted property
                mItem.setmGifted(isChecked);
            }
        });

        //Submit Button Acts like hitting the back button and adding the purchase
        mSubmitButton = (Button) findViewById(R.id.submit_purchase);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedOptions(mItem.getmBuyer()!= null, mItem.getmConsumer() != null, getBaseContext());
            }
        });

        mDeleteButton = (Button) findViewById(R.id.delete_item);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMessage();
            }
        });

        //Toast.makeText(getContext(), "Purchase id is "+ mItem.getmTransactionID(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_BUYER&&data !=null){
            buyer = true;
            Uri contactUri= data.getData();
            //Specify which fields you want your query to return values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query - contactUri is like a "where" clause here
            Cursor c = this.getContentResolver()
                    .query(contactUri, queryFields, null,null,null);
            try {
                //Double check that you got results
                if (c.getCount() == 0) {
                    return;
                }
                //pullout the first column of the first row of data-that is your suspect's name
                c.moveToFirst();
                String buyer = c.getString(0);
                mItem.setmBuyer(buyer);
                mBuyer.setText(buyer);
            }finally {
                c.close();
            }
        }
        if (requestCode==REQUEST_RECEIVER&&data !=null){
            receiver = true;
            Uri contactUri= data.getData();
            //Specify which fields you want your query to return values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query - contactUri is like a "where" clause here
            Cursor c = this.getContentResolver()
                    .query(contactUri, queryFields, null,null,null);
            try {
                //Double check that you got results
                if (c.getCount() == 0) {
                    return;
                }
                //pullout the first column of the first row of data-that is your suspect's name
                c.moveToFirst();
                String receiver = c.getString(0);
                mItem.setmConsumer(receiver);
                mReciever.setText(receiver);
            }finally {
                c.close();
            }
        }
    }

    private void showDialogMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Place");
        builder.setMessage("Are you sure you want to delete this item?");

        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                DatabaseHelper dbh = new DatabaseHelper(getBaseContext());
                SQLiteDatabase db = dbh.getWritableDatabase();
                db.delete(DbSchema.TableInfo.Item_Table, DbSchema.TableInfo.Itemized_Purchase.IUUID + " = ?", new String[] {mItem.getID().toString()});
                dialog.cancel();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onBackPressedOptions(boolean buyer, boolean reciever, Context context){
        if (!buyer) {
            Toast.makeText(context, "Must choose a buyer.", Toast.LENGTH_SHORT).show();
        } else if ( !reciever) {
            Toast.makeText(context, "Must choose a receiver.", Toast.LENGTH_SHORT).show();
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed(){
        if (!buyer) {
            Toast.makeText(getBaseContext(), "Must choose a buyer.", Toast.LENGTH_SHORT).show();
        } else if ( !receiver) {
            Toast.makeText(getBaseContext(), "Must choose a receiver.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

}






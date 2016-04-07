package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nicole Geiger on 3/14/2016.
 */
public class AddPlaceActivity extends AppCompatActivity {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    private EditText Place_Name;
    private String getPlace_Name;

    private EditText Place_Address;
    private String getPlace_Address;

    private Button place_googlefind;
    private Button place_submit;
    private Button mCancel;
    private Context db = this;

    private UUID purchaseId;
    private Purchase mPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        purchaseId = (UUID) bundle.getSerializable("purchase_id");
        DatabaseHelper dbh = new DatabaseHelper(getBaseContext());
        mPurchase = dbh.purchaseByUUID(purchaseId);




        setContentView(R.layout.place_insert_layout);
        Place_Name = (EditText) findViewById(R.id.Place_Name);
        Place_Address = (EditText) findViewById(R.id.Place_Address);
        place_googlefind = (Button) findViewById(R.id.Place_googlefind);
        place_submit = (Button) findViewById(R.id.Place_Button);
        mCancel = (Button) findViewById(R.id.cancel_Button);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        place_googlefind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FindPlace.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        place_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convert input info to strings
                getPlace_Name = Place_Name.getText().toString();
                getPlace_Address = Place_Address.getText().toString();
                if (getPlace_Name.length() > 0) {
                    //Adds location and address to the database
                    DatabaseHelper DB = new DatabaseHelper(db);
                    long position =DB.addPlace(DB, getPlace_Name, getPlace_Address);
                    Toast.makeText(getBaseContext(), "Added " + getPlace_Name + " at " + getPlace_Address + ".", Toast.LENGTH_LONG).show();

                    //clear out fields
                    Place_Name.setText("");
                    Place_Address.setText("");

                    mPurchase.setPlace((int)position);
                    String uuid = mPurchase.getID().toString();


                    ContentValues values = new ContentValues();
                    values.put(DbSchema.TableInfo.Deal.UUID, mPurchase.getID().toString());
                    values.put(DbSchema.TableInfo.Deal.DoD, mPurchase.getDate().getTime());
                    values.put(DbSchema.TableInfo.Deal.BTID, mPurchase.getType());
                    values.put(DbSchema.TableInfo.Deal.PID, mPurchase.getPlace());
                    SQLiteDatabase database = DB.getReadableDatabase();
                            database.update(DbSchema.TableInfo.Deal_Table, values,
                            DbSchema.TableInfo.Deal.UUID + " = ?", new String[]{uuid});
                    //return to previous screen
                    finish();
                }
            }
        });

    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode,
        Intent data) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    String address = data.getStringExtra("Address");
                    String name = data.getStringExtra("Name");
                    getPlace_Address = address;
                    getPlace_Name = name;
                    Place_Name.setText(getPlace_Name);
                    Place_Address.setText(getPlace_Address);

                }
            }
        }
    }



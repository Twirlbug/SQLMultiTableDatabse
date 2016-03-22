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
 */
public class AddPlaceActivity extends Activity {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    EditText Place_Name;
    String getPlace_Name;

    EditText Place_Address;
    String getPlace_Address;

    Button place_googlefind;
    Button place_submit;
    Context db = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_insert_layout);
        Place_Name = (EditText) findViewById(R.id.Place_Name);
        Place_Address = (EditText) findViewById(R.id.Place_Address);
        place_googlefind = (Button) findViewById(R.id.Place_googlefind);
        place_submit = (Button) findViewById(R.id.Place_Button);

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
                    DB.addPlace(DB, getPlace_Name, getPlace_Address);
                    Toast.makeText(getBaseContext(), "Added " + getPlace_Name + " at " + getPlace_Address + ".", Toast.LENGTH_LONG).show();

                    //clear out fields
                    Place_Name.setText("");
                    Place_Address.setText("");

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



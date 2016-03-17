package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Context;
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
public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{
    EditText Place_Name;
    String getPlace_Name;

    EditText Place_Address;
    String getPlace_Address;

    Spinner Place_Spinner;
    Button place_submit;
    Context db = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_insert_layout);
        Place_Name = (EditText) findViewById(R.id.Place_Name);
        Place_Address = (EditText) findViewById(R.id.Place_Address);
        Place_Spinner = (Spinner) findViewById(R.id.Place_Spinner_purchase);
        place_submit = (Button) findViewById(R.id.Place_Button);

        // Spinner click listener
        // Loading spinner data from database
        //todo for testing
        Place_Spinner.setOnItemSelectedListener(this);
        loadSpinnerData();

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

                    //reload drop down field
                    //todo for testing
                    loadSpinnerData();
                    //finish();
                }
            }
        });
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getPlaceLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        Place_Spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label + " in " + position ,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Nothing selected do nothing

    }
}


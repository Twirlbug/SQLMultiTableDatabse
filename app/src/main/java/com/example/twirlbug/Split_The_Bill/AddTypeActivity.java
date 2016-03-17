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
 */
public class AddTypeActivity extends Activity {
    EditText Type_Name;
    String getType_Name;



    Button Type_submit;
    Context db = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_insert_layout);
        Type_Name = (EditText) findViewById(R.id.Type_Name);
        Type_submit = (Button) findViewById(R.id.Type_Button);



        Type_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convert input info to strings
                getType_Name = Type_Name.getText().toString();
                if (getType_Name.length() > 0) {
                    //Adds Type to the database
                    DatabaseHelper DB = new DatabaseHelper(db);
                    DB.addType(DB, getType_Name);
                    Toast.makeText(getBaseContext(), "Added " + getType_Name, Toast.LENGTH_LONG).show();

                    //clear out fields
                    Type_Name.setText("");

                    //return to previous screen
                    finish();

                }
            }
        });
    }


}

package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

/**
 * Created by Twirlbug on 3/17/2016.
 */
public class EditPlaceActivity extends AppCompatActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    private EditText Place_Name;
    private String getPlace_Name;

    private EditText Place_Address;
    private String getPlace_Address;
    private Button mCancel;

    int getPlace_Id;

    private Button place_googlefind;
    private Button place_submit;
    private Button place_delete;
    private Context db = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_edit_layout);
        Place_Name = (EditText) findViewById(R.id.Place_Name);
        Place_Address = (EditText) findViewById(R.id.Place_Address);
        place_googlefind = (Button) findViewById(R.id.Place_googlefind);
        place_submit = (Button) findViewById(R.id.Place_Button);
        place_delete = (Button) findViewById(R.id.Place_delete);
        mCancel = (Button) findViewById(R.id.cancel_Button);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle b = getIntent().getExtras();
        getPlace_Id = b.getInt("id");
        if (getPlace_Id == 0) {
            Toast.makeText(getBaseContext(), "Cannot Edit/Delete Place None", Toast.LENGTH_LONG).show();
            finish();
        }

        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
        Place_Name.setText(dbh.PlaceToString(getPlace_Id));
        Place_Address.setText(dbh.PlaceToAddress(getPlace_Id));





        place_googlefind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReplacePlace.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        place_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convert input info to strings
                String oldname = getPlace_Name;
                getPlace_Name = Place_Name.getText().toString();
                getPlace_Address = Place_Address.getText().toString();
                if (getPlace_Name.length() > 0) {
                    //Adds location and address to the database
                    DatabaseHelper dbh = new DatabaseHelper(db);
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DbSchema.TableInfo.Place.PID, getPlace_Id);
                    values.put(DbSchema.TableInfo.Place.PN, getPlace_Name);
                    values.put(DbSchema.TableInfo.Place.PA, getPlace_Address);
                    db.update(DbSchema.TableInfo.Place_Table, values,
                            DbSchema.TableInfo.Place.PID + " = ?",
                            new String[] {Integer.toString(getPlace_Id)});

                    Toast.makeText(getBaseContext(), "Edited " + oldname + " to " + getPlace_Name + ".", Toast.LENGTH_LONG).show();

                    //clear out fields
                    Place_Name.setText("");
                    Place_Address.setText("");

                    //return to previous screen
                    finish();
                }
            }
        });

        place_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMessage();

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

    private void showDialogMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Place");
        builder.setMessage("Are you sure you want to delete this place? WARNING: ALL ENTRIES WITH THIS PLACE WILL BE CHANGED TO PLACE NONE");

        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //todo delete cascade Current type and change all occurrences of this to None/ id 0
                DatabaseHelper dbh = new DatabaseHelper(db);
                SQLiteDatabase db = dbh.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DbSchema.TableInfo.Deal.PID, "0");
                db.update(DbSchema.TableInfo.Deal_Table, values, DbSchema.TableInfo.Deal.PID + " = ?", new String[]{Integer.toString(getPlace_Id)});
                db.delete(DbSchema.TableInfo.Place_Table, DbSchema.TableInfo.Place.PID + " = " + getPlace_Id, null);
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
}


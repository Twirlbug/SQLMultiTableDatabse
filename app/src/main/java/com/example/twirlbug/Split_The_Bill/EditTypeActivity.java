package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
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
public class EditTypeActivity extends AppCompatActivity {
    private EditText Type_Name;
    private String getType_Name;
    private int getType_Id;

    private Button Type_submit;
    private Button Type_delete;
    private Context db = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_edit_layout);
        Type_Name = (EditText) findViewById(R.id.Type_Name);
        Type_submit = (Button) findViewById(R.id.Type_Button);
        Type_delete = (Button) findViewById(R.id.Type_delete);

        Bundle b = getIntent().getExtras();
        getType_Id = b.getInt("id");
        //Toast.makeText(getBaseContext(), "Type id: " + getType_Id, Toast.LENGTH_SHORT).show();
        //Does not allow editing of base type none
        if (getType_Id == 0) {
            Toast.makeText(getBaseContext(), "Cannot Edit/Delete Type None", Toast.LENGTH_LONG).show();
            finish();
        }

        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
        Type_Name.setText(dbh.TypeToString(getType_Id));

        Type_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convert input info to strings
                String oldName = getType_Name;
                getType_Name = Type_Name.getText().toString();
                if (getType_Name.length() > 0) {
                    //Adds Type to the database
                    DatabaseHelper dbh = new DatabaseHelper(db);
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DbSchema.TableInfo.Type.TID, getType_Id);
                    values.put(DbSchema.TableInfo.Type.TN, getType_Name);
                    db.update(DbSchema.TableInfo.Type_Table, values,
                            DbSchema.TableInfo.Type.TID + " = ?",
                            new String[]{Integer.toString(getType_Id)});

                    Toast.makeText(getBaseContext(), "Changed " + oldName + " to " + getType_Name, Toast.LENGTH_LONG).show();

                    //clear out fields
                    Type_Name.setText("");

                    //return to previous screen
                    finish();

                }
            }
        });

        Type_Name.setText(dbh.TypeToString(getType_Id));

        Type_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMessage();

            }
        });
    }


    private void showDialogMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Type");
        builder.setMessage("Are you sure you want to delete this type? WARNING: ALL ENTRIES WITH THIS TYPE WILL BE CHANGED TO TYPE NONE");

        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                DatabaseHelper dbh = new DatabaseHelper(db);
                SQLiteDatabase db = dbh.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DbSchema.TableInfo.Deal.BTID, "0");
                db.update(DbSchema.TableInfo.Deal_Table, values, DbSchema.TableInfo.Deal.BTID + " = ?", new String[]{Integer.toString(getType_Id)});
                db.delete(DbSchema.TableInfo.Type_Table, DbSchema.TableInfo.Type.TID + " = " + getType_Id, null);
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


package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
public class AddTypeActivity extends AppCompatActivity {
    private EditText Type_Name;
    private String getType_Name;

    private UUID purchaseId;
    private Purchase mPurchase;

    private Button Type_submit;
    private Button mCancel;
    Context db = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        purchaseId = (UUID) bundle.getSerializable("purchase_id");
        DatabaseHelper dbh = new DatabaseHelper(getBaseContext());
        mPurchase = dbh.purchaseByUUID(purchaseId);

        setContentView(R.layout.type_insert_layout);
        Type_Name = (EditText) findViewById(R.id.Type_Name);
        Type_submit = (Button) findViewById(R.id.Type_Button);
        mCancel = (Button) findViewById(R.id.cancel_Button);


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Type_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convert input info to strings
                getType_Name = Type_Name.getText().toString();
                if (getType_Name.length() > 0) {
                    //Adds Type to the database
                    DatabaseHelper DB = new DatabaseHelper(db);
                    long position = DB.addType(DB, getType_Name);
                    Toast.makeText(getBaseContext(), "Added " + getType_Name, Toast.LENGTH_LONG).show();

                    //clear out fields
                    Type_Name.setText("");

                    mPurchase.setType((int)position);
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


}

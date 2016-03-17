package com.example.twirlbug.Split_The_Bill.database;

/**
 * Created by Nicole Geiger on 3/14/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.twirlbug.Split_The_Bill.database.DbSchema.TableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int database_version = 1;


    public DatabaseHelper(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database operations", "Database created");
    }

/* Create Tables for Purchase, Meal, Type and Places */
    @Override
    public void onCreate(SQLiteDatabase db){
        //Create the Meal Table
        db.execSQL("create table " + TableInfo.Deal_Table + "("
                        + TableInfo.Deal.ID + " integer primary key autoincrement, "
                        + TableInfo.Deal.UUID + ", "
                        + TableInfo.Deal.PID + " integer, "
                        + TableInfo.Deal.DoD + ", "
                        + TableInfo.Deal.BTID + " integer "
                        //foreign key decleration once all tables are in ( ONLY IF NEEDED )
                            //+" FOREIGN KEY ("+TableInfo.MRID+") REFERENCES "+TableInfo.Place_Table+"("+TableInfo.RID+")"
                            //+" FOREIGN KEY ("+TableInfo.MTID+") REFERENCES "+TableInfo.Type_Table+"("+TableInfo.TID+")"
                        +");"
        ); //sends Meal Table Create Command

        //Create Place Table
        db.execSQL("create table " + TableInfo.Place_Table + "("
                        + TableInfo.Place.PID + " integer primary key autoincrement, "
                        + TableInfo.Place.PA + ", "
                        + TableInfo.Place.PN
                        +");"
        );//sends Place Table Create Command

        //Create Type Table
        db.execSQL("create table " + TableInfo.Type_Table + "("
                        + TableInfo.Type.TID + " integer primary key autoincrement, "
                        + TableInfo.Type.TUUID + ", "
                        + TableInfo.Type.TN
                        +");"
        );//sends Type Table Create Command

        //Create Purchase Table
        db.execSQL("create table " + TableInfo.Purchase_Table + "("
                        + TableInfo.Itemized_Purchase.PID + " integer primary key autoincrement, "
                        + TableInfo.Itemized_Purchase.PC + ", "
                        + TableInfo.Itemized_Purchase.PB + ", "
                        + TableInfo.Itemized_Purchase.PP + " double, "
                        + TableInfo.Itemized_Purchase.MID + " integer "
                        + ");"
        );//sends Type Table Create Command

        //enter in a place 0 for type and place with component of none
        ContentValues Type = new ContentValues();
        Type.put(TableInfo.Type.TID, "0");
        Type.put(TableInfo.Type.TN, "None");
        db.insert(TableInfo.Type_Table, null, Type);

        ContentValues Place = new ContentValues();
        Place.put(TableInfo.Place.PID, "0");
        Place.put(TableInfo.Place.PN, "None");
        Place.put(TableInfo.Place.PA, "N/A");
        db.insert(TableInfo.Place_Table, null, Place);

        Log.d("Database operations", "Tables created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }



    public void addPlace(DatabaseHelper dop, String place_name, String place_address){
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.Place.PN, place_name);
        cv.put(TableInfo.Place.PA, place_address);
        long r = db.insert(TableInfo.Place_Table, null, cv);
        Log.d("Database operations", "Inserted into Place");
    }


    //listing for drop down session of Places
    public List<String> getPlaceLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TableInfo.Place_Table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public UUID PlaceToUUID(int name){
        UUID PlaceUUID; //Initialize to be -1 if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.Place.PUUID + " FROM " + TableInfo.Place_Table + " WHERE " + TableInfo.Place.PID + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            PlaceUUID= UUID.fromString(cursor.getString(cursor.getColumnIndex(TableInfo.Place.PN)));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            PlaceUUID = UUID.fromString("ERROR");
        }
        cursor.close();
        return PlaceUUID;
    }

    public String PlaceToString(int name){
        String TypeName; //Initialize Type Name
        // Select Table ID where Table name is same as string name Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TableInfo.Place_Table,
                new String[]{TableInfo.Place.PN},
                TableInfo.Place.PID + " = ?",
                new String[]{Integer.toString(name)},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            TypeName= cursor.getString(cursor.getColumnIndex(TableInfo.Place.PN));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            TypeName = "ERROR";
        }
        cursor.close();
        return TypeName;
    }

    public void addType(DatabaseHelper dop, String name){
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.Type.TN, name);
        long r = db.insert(TableInfo.Type_Table, null, cv);
        Log.d("Database operations", "Inserted into Type");
    }

    public String getTypeName(UUID id){
        //todo fill in
        String name = "";

        return name;
    }

    public int TypeToInt(String name){
        //todo fill in
        return 0;
    }

    public String TypeToString(int name){
        String TypeName; //Initialize Type Name
        // Select Table ID where Table name is same as string name Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TableInfo.Type_Table,
                new String[] {TableInfo.Type.TN},
                TableInfo.Type.TID + " = ?",
                new String[] {Integer.toString(name)},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            TypeName= cursor.getString(cursor.getColumnIndex(TableInfo.Type.TN));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            TypeName = "ERROR";
        }
        cursor.close();
        return TypeName;
    }



    public void addPurchase(){
        //todo
    }
}

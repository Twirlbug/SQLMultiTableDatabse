package com.example.twirlbug.Split_The_Bill.database;

/**
 * Created by Nicole Geiger on 3/14/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.twirlbug.Split_The_Bill.database.DbSchema.TableInfo;

import java.util.ArrayList;
import java.util.List;

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
        db.execSQL("create table " + TableInfo.Meal_Table + "("
                        + TableInfo.MID + " integer primary key autoincrement, "
                        + TableInfo.MRID + " integer, "
                        + TableInfo.DoM + ", "
                        + TableInfo.ToM + ", "
                        + TableInfo.MTID + " integer "
                        //foreign key decleration once all tables are in ( ONLY IF NEEDED )
                            //+" FOREIGN KEY ("+TableInfo.MRID+") REFERENCES "+TableInfo.Place_Table+"("+TableInfo.RID+")"
                            //+" FOREIGN KEY ("+TableInfo.MTID+") REFERENCES "+TableInfo.Type_Table+"("+TableInfo.TID+")"
                        +");"
        ); //sends Meal Table Create Command

        //Create Place Table
        db.execSQL("create table " + TableInfo.Place_Table + "("
                        + TableInfo.RID + " integer primary key autoincrement, "
                        + TableInfo.RN + ", "
                        + TableInfo.RA
                        +");"
        );//sends Place Table Create Command

        //Create Type Table
        db.execSQL("create table " + TableInfo.Type_Table + "("
                        + TableInfo.TID + " integer primary key autoincrement, "
                        + TableInfo.TN
                        +");"
        );//sends Type Table Create Command

        //Create Purchase Table
        db.execSQL("create table " + TableInfo.Purchase_Table + "("
                        + TableInfo.PID + " integer primary key autoincrement, "
                        + TableInfo.PC + ", "
                        + TableInfo.PB + ", "
                        + TableInfo.PP + " double, "
                        + TableInfo.PMID + " integer "
                        + ");"
        );//sends Type Table Create Command

        Log.d("Database operations", "Tables created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    public void addMeal(DatabaseHelper dop, int PlaceID, String TypeID){
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(TableInfo.RN, place_name);
        //cv.put(TableInfo.RA, place_address);
        //long r = db.insert(TableInfo.Place_Table, null, cv);
        //Log.d("Database operations", "Inserted into Place");
        //todo edit for all entry items
    }



    public void addPlace(DatabaseHelper dop, String place_name, String place_address){
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.RN, place_name);
        cv.put(TableInfo.RA, place_address);
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

    public int PlaceToInt(String name){
        int TypeID = -1; //Initialize to be -1 if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.RID + " FROM " + TableInfo.Place_Table + " WHERE " + TableInfo.RN + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if the query returns something make that an int and return it
        if (cursor.moveToFirst()){
            TypeID = Integer.parseInt(cursor.getString(1));
        }
        return TypeID;
    }

    public String PlaceToString(int name){
        String TypeName = " "; //Initialize to be blank if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.RN + " FROM " + TableInfo.Place_Table + " WHERE " + TableInfo.RID + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if the query returns something make that into the return string
        if (cursor.moveToFirst()){
            TypeName = cursor.getString(1);
        }
        return TypeName;
    }



    public void addType(){
        //todo
    }

    public int TypeToInt(String name){
        int TypeID = -1; //Initialize to be -1 if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.TID + " FROM " + TableInfo.Type_Table + " WHERE " + TableInfo.TN + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if the query returns something make that an int and return it
        if (cursor.moveToFirst()){
                TypeID = Integer.parseInt(cursor.getString(1));
        }
        return TypeID;
    }

    public String TypeToString(int name){
        String TypeName = " "; //Initialize to be blank if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.TN + " FROM " + TableInfo.Type_Table + " WHERE " + TableInfo.TID + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if the query returns something make that into the return string
        if (cursor.moveToFirst()){
            TypeName = cursor.getString(1);
        }
        return TypeName;
    }



    public void addPurchase(){
        //todo
    }
}

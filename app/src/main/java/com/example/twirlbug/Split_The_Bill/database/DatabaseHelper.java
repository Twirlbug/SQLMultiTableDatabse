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

import com.example.twirlbug.Split_The_Bill.Item;
import com.example.twirlbug.Split_The_Bill.Purchase;
import com.example.twirlbug.Split_The_Bill.PurchaseCursorWrapper;
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
                        + TableInfo.Type.TN
                        +");"
        );//sends Type Table Create Command

        //Create Purchase Table
        db.execSQL("create table " + TableInfo.Item_Table + "("
                        + TableInfo.Itemized_Purchase.IID + " integer primary key autoincrement, "
                        + TableInfo.Itemized_Purchase.IUUID +", "
                        + TableInfo.Itemized_Purchase.IN + ", "
                        + TableInfo.Itemized_Purchase.IC + ", "
                        + TableInfo.Itemized_Purchase.IB + ", "
                        + TableInfo.Itemized_Purchase.IG + ", "
                        + TableInfo.Itemized_Purchase.ICents + ", "
                        + TableInfo.Itemized_Purchase.IDollars + ", "
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
        Place.put(TableInfo.Place.PA, "None");
        db.insert(TableInfo.Place_Table, null, Place);

        Log.d("Database operations", "Tables created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    public UUID TransToUUID(int id){
        String uuid = Integer.toString(id);
        String dbid;
        String selectQuery = "SELECT " + TableInfo.Deal.UUID + " FROM " + TableInfo.Deal_Table + " WHERE " + TableInfo.Deal.ID + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{uuid});

        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            dbid= cursor.getString(cursor.getColumnIndex(TableInfo.Deal.UUID));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            dbid = "ERROR";
        }
        cursor.close();
        return UUID.fromString(dbid);

    }

    public int TransToMainID(UUID id){
        String uuid = id.toString();
        int dbid;
        String selectQuery = "SELECT " + TableInfo.Deal.ID + " FROM " + TableInfo.Deal_Table + " WHERE " + TableInfo.Deal.UUID + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{uuid});

        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            dbid= cursor.getInt(cursor.getColumnIndex(TableInfo.Deal.ID));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            dbid = 0;
        }
        cursor.close();
        return dbid;

    }

    public long addPlace(DatabaseHelper dop, String place_name, String place_address) {
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.Place.PN, place_name);
        cv.put(TableInfo.Place.PA, place_address);
        long r = db.insert(TableInfo.Place_Table, null, cv);
        Log.d("Database operations", "Inserted into Place");
        long lastid = 0;
        String query = "SELECT " +TableInfo.Place.PID + " from " + TableInfo.Place_Table+ " order by "+ TableInfo.Place.PID + " DESC limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastid = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        c.close();
        return lastid;
    }

    public String PlaceToAddress(int name){
        String Address; //Initialize to be -1 if all else fails
        // Select Table ID where Table name is same as string name Query
        String selectQuery = "SELECT " + TableInfo.Place.PA + " FROM " + TableInfo.Place_Table + " WHERE " + TableInfo.Place.PID + " = " + name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            Address= cursor.getString(cursor.getColumnIndex(TableInfo.Place.PA));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            Address = "ERROR";
        }
        cursor.close();
        return Address;
    }

    public String PlaceToString(int name) {
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

    public long addType(DatabaseHelper dop, String name) {
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long lastid = 0;
        cv.put(TableInfo.Type.TN, name);
        long r = db.insert(TableInfo.Type_Table, null, cv);
        Log.d("Database operations", "Inserted into Type");

        String query = "SELECT " +TableInfo.Type.TID + " from " + TableInfo.Type_Table+ " order by "+ TableInfo.Type.TID + " DESC limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastid = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        return lastid;
    }

    public String TypeToString(int name){
        String TypeName; //Initialize Type Name
        // Select Table ID where Table name is same as string name Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TableInfo.Type_Table,
                new String[]{TableInfo.Type.TN},
                TableInfo.Type.TID + " = ?",
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

public Purchase purchaseByUUID (UUID id) {
    SQLiteDatabase db = this.getReadableDatabase();

    String whereClause = DbSchema.TableInfo.Deal.UUID + "= ?";
    String[] whereargs = new String[]{id.toString()};
    Cursor cursor =  db.query(
            DbSchema.TableInfo.Deal_Table,
            null,
            whereClause,
            whereargs,
            null, //groupby
            null, //having
            null //orderBy
    );

    PurchaseCursorWrapper pcw = new PurchaseCursorWrapper(cursor);


    try{
        if(pcw.getCount() == 0){
            return null;
        }
        pcw.moveToFirst();
        return pcw.getPurchase();
    }finally {
        pcw.close();
        cursor.close();
    }

}
}

package com.example.twirlbug.Split_The_Bill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemLister {
    public static ItemLister sItemLister;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemLister get(Context context) {
        if (sItemLister==null){
            sItemLister = new ItemLister(context);
        }
        return sItemLister;
    }

    private ItemLister(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addItem(Item p){
        ContentValues values = getContentValues(p);
        mDatabase.insert(DbSchema.TableInfo.Item_Table, null, values);
        Log.d("Database entry", "New Item added");
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        ItemCursorWrapper cursor = queryItems(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return items;
    }

    public List<Item> getItems(int transId) {
        List<Item> items = new ArrayList<>();

        ItemCursorWrapper cursor = queryItems(DbSchema.TableInfo.Itemized_Purchase.MID + "= ?",
                new String[] {Integer.toString(transId)});

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return items;
    }


    public List<Item> getItems(String person1, String person2, Context context) {
        List<Item> items = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT * FROM " + DbSchema.TableInfo.Item_Table + " WHERE ("
                + DbSchema.TableInfo.Itemized_Purchase.IC + " = ?" + " AND "
                + DbSchema.TableInfo.Itemized_Purchase.IB + " = ?" + ") OR ("
                + DbSchema.TableInfo.Itemized_Purchase.IC + " = ?" + " AND "
                + DbSchema.TableInfo.Itemized_Purchase.IB + " = ? )";
        DatabaseHelper dbh = new DatabaseHelper(context);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{person1, person2, person2, person1});
        ItemCursorWrapper cursor = new ItemCursorWrapper(c);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
            c.close();
        }
        return items;
    }

    public List<Item> getItems(String person1, Context context) {
        List<Item> items = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT * FROM " + DbSchema.TableInfo.Item_Table + " WHERE "
                + DbSchema.TableInfo.Itemized_Purchase.IB + " = ? " + " OR "
                + DbSchema.TableInfo.Itemized_Purchase.IC + " = ? ";
        DatabaseHelper dbh = new DatabaseHelper(context);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{person1, person1});
        ItemCursorWrapper cursor = new ItemCursorWrapper(c);


        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
            c.close();
        }
        return items;
    }


    public Item getItem(UUID id){
        ItemCursorWrapper cursor = queryItems(
                DbSchema.TableInfo.Itemized_Purchase.IUUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        }finally {
            cursor.close();
        }
    }

    public void updateItem(Item item){
        String uuid = item.getID().toString();
        ContentValues values = getContentValues(item);

        mDatabase.update(DbSchema.TableInfo.Item_Table, values,
                DbSchema.TableInfo.Itemized_Purchase.IUUID + " = ?", new String[] { uuid});
    }

    private static ContentValues getContentValues(Item item){
        ContentValues values = new ContentValues();
        values.put(DbSchema.TableInfo.Itemized_Purchase.IUUID, item.getID().toString());
        values.put(DbSchema.TableInfo.Itemized_Purchase.IN, item.getmName());
        values.put(DbSchema.TableInfo.Itemized_Purchase.IC, item.getmConsumer());
        values.put(DbSchema.TableInfo.Itemized_Purchase.IB, item.getmBuyer());
        values.put(DbSchema.TableInfo.Itemized_Purchase.ICents, item.getmCents());
        values.put(DbSchema.TableInfo.Itemized_Purchase.IDollars, item.getmDollars());
        values.put(DbSchema.TableInfo.Itemized_Purchase.IG, item.getmGifted());
        values.put(DbSchema.TableInfo.Itemized_Purchase.MID, item.getmTransactionID());
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereargs){
        Cursor cursor = mDatabase.query(
                DbSchema.TableInfo.Item_Table,
                null,
                whereClause,
                whereargs,
                null, //groupby
                null, //having
                null //orderBy
        );
        return new ItemCursorWrapper(cursor);
    }
}

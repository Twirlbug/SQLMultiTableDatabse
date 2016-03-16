package com.example.twirlbug.Split_The_Bill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema.TableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseLister {
    public static PurchaseLister spurchaseLister;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static PurchaseLister get(Context context) {
        if (spurchaseLister==null){
            spurchaseLister = new PurchaseLister(context);
        }
        return spurchaseLister;
    }

    private PurchaseLister(Context context){
       mContext = context.getApplicationContext();
       mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void addPurchase(Purchase p){
        ContentValues values = getContentValues(p);

        mDatabase.insert(TableInfo.Transaction_Table, null, values);
    }

    public List<Purchase> getPurchases() {
        List<Purchase> purchases = new ArrayList<>();

        PurchaseCursorWrapper cursor = queryPurchases(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                purchases.add(cursor.getPurchase());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return purchases;
    }

    public Purchase getPurchase(UUID id){
        PurchaseCursorWrapper cursor = queryPurchases(
                TableInfo.Trans.TrUUID + " = ?",
                new String[] { id.toString() }
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPurchase();
        }finally {
            cursor.close();
        }
    }

    public void updatePurchase(Purchase purchase){
        String uuidString = purchase.getID().toString();
        ContentValues values = getContentValues(purchase);

        mDatabase.update(TableInfo.Transaction_Table, values,
                TableInfo.Trans.TrUUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Purchase purchase){
        ContentValues values = new ContentValues();
        values.put(TableInfo.Trans.TrUUID, purchase.getID().toString());
        values.put(TableInfo.Trans.TrRID, purchase.getPlace());
        values.put(TableInfo.Trans.DoM, purchase.getDate().getTime());
        values.put(TableInfo.Trans.TrTID, purchase.getType());

        return values;
    }

    private PurchaseCursorWrapper queryPurchases(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                TableInfo.Transaction_Table,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having,
                null //orderby
        );
        return new PurchaseCursorWrapper(cursor);
    }
}

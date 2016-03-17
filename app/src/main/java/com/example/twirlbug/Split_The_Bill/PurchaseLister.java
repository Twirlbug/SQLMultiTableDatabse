package com.example.twirlbug.Split_The_Bill;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseLister {
    public static PurchaseLister spurchaseLister;

    private static List<Purchase> mPurchases;
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
        mDatabase = new DatabaseHelper(mContext)
                        .getWritableDatabase();
        mPurchases = new ArrayList<>();
    }

    public void addPurchase(Purchase p){
        //mPurchases.add(p);
        ContentValues values = getContentValues(p);
        mDatabase.insert(DbSchema.TableInfo.Deal_Table, null, values);
    }

    public List<Purchase> getPurchases() {
        return mPurchases;
    }

    public Purchase getPurchase(UUID id){
        for( Purchase purchase : mPurchases){
            if (purchase.getID().equals(id)){
                return purchase;
            }
        }
        return null;
    }

    public void updatePurchase(Purchase purchase){
        String uuid = purchase.getID().toString();
        ContentValues values = getContentValues(purchase);

        mDatabase.update(DbSchema.TableInfo.Deal_Table, values,
                DbSchema.TableInfo.Deal.UUID + " = ?", new String[] { uuid});
    }

    private static ContentValues getContentValues(Purchase purchase){
        ContentValues values = new ContentValues();
        values.put(DbSchema.TableInfo.Deal.UUID, purchase.getID().toString());
        values.put(DbSchema.TableInfo.Deal.DoD, purchase.getDate().getTime());
        values.put(DbSchema.TableInfo.Deal.BTID, purchase.getType());
        values.put(DbSchema.TableInfo.Deal.PID, purchase.getPlace());

        return values;
    }
}

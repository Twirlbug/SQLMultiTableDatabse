package com.example.twirlbug.Split_The_Bill;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;

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
        mPurchases.add(p);
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
}

package com.example.twirlbug.Split_The_Bill;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseLister {
    public static PurchaseLister spurchaseLister;

    private static List<Purchase> mPurchases;

    public static PurchaseLister get(Context context) {
        if (spurchaseLister==null){
            spurchaseLister = new PurchaseLister(context);
        }
        return spurchaseLister;
    }

    private PurchaseLister(Context context){
        mPurchases = new ArrayList<>();
        for (int i=0; i <100; i++){
            Purchase purchase = new Purchase();
            mPurchases.add(purchase);
        }
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

package com.example.twirlbug.Split_The_Bill;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.twirlbug.Split_The_Bill.database.DbSchema.TableInfo;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/16/2016.
 */
public class PurchaseCursorWrapper extends CursorWrapper {

    public PurchaseCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Purchase getPurchase(){
        String uuidString = getString(getColumnIndex(TableInfo.Trans.TrUUID));
        long date = getLong(getColumnIndex(TableInfo.Trans.DoM));
        int type = getInt(getColumnIndex(TableInfo.Trans.TrTID));
        int place = getInt(getColumnIndex(TableInfo.Trans.TrRID));

        Purchase purchase = new Purchase(UUID.fromString(uuidString));
        purchase.setDate(new Date(date));
        purchase.setType(type);
        purchase.setPlace(place);

        return purchase;
    }
}


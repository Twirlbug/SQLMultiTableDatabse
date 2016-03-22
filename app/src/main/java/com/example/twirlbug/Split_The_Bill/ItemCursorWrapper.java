package com.example.twirlbug.Split_The_Bill;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemCursorWrapper extends CursorWrapper{
    
    public ItemCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Item getItem(){
        String uuidString = getString(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IUUID));
        String name = getString(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IN));
        String consumer =getString(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IC));
        String buyer = getString(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IB));
        int dollars = getInt(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IDollars));
        int cents = getInt(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.ICents));
        int gift = getInt(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.IG));
        int transId = getInt(getColumnIndex(DbSchema.TableInfo.Itemized_Purchase.MID));


        Item item = new Item(UUID.fromString(uuidString));
        item.setmName(name);
        item.setmConsumer(consumer);
        item.setmBuyer(buyer);
        item.setmDollars(dollars);
        item.setmCents(cents);
        item.setmGifted(gift !=0);
        item.setmTransactionID(transId);

        return item;
    }
}




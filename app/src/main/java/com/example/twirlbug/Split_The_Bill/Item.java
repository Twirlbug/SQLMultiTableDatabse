package com.example.twirlbug.Split_The_Bill;

import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.io.File;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class Item {
    private UUID mId;
    private String mName;
    private String mConsumer;
    private String mBuyer;
    private int mDollars;
    private int mCents;
    private boolean mGifted;
    private int mTransactionID;

    public Item(){
        this(UUID.randomUUID());
    }

    public Item(UUID id){
        mId = id;
    }

    public UUID getID(){
        return mId;

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmConsumer() {
        return mConsumer;
    }

    public void setmConsumer(String mConsumer) {
        this.mConsumer = mConsumer;
    }

    public String getmBuyer() {
        return mBuyer;
    }

    public void setmBuyer(String mBuyer) {
        this.mBuyer = mBuyer;
    }

    public int getmDollars() {
        return mDollars;
    }

    public void setmDollars(int mDollars) {
        this.mDollars = mDollars;
    }

    public int getmCents() {
        return mCents;
    }

    public void setmCents(int mCents) {
        this.mCents = mCents;
    }

    public boolean getmGifted() {
        return mGifted;
    }

    public void setmGifted(boolean mGifted) {
        this.mGifted = mGifted;
    }

    public int getmTransactionID() {
        return mTransactionID;
    }

    public void setmTransactionID(int mTransactionID) {
        this.mTransactionID = mTransactionID;
    }

}

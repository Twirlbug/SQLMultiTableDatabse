package com.example.twirlbug.Split_The_Bill;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class Purchase {

    private UUID mID;
    private Date mDate;
    private int mType;
    private int mPlace;

    public Purchase(){
        this(UUID.randomUUID());
    }

    public Purchase(UUID id) {
        mID = id;
        mDate = new Date();
        mType = 0;
        mPlace = 0;
    }

    public UUID getID(){
        return mID;

    }

    public Date getDate() {
        return mDate;
    }

    public String getDateS() {

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = formatter.format(mDate);
        return s;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getPlace() {
        return mPlace;
    }

    public void setPlace(int place) {
        mPlace = place;
    }
}

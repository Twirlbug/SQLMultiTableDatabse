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
    private String mType;
    private String mPlace;

    public Purchase(){
        mID = UUID.randomUUID();
        mDate = new Date();
        mType = "Food";
        mPlace = "Cafe";
    }

    public UUID getID(){
        return mID;

    }

    public Date getDate() {
        return mDate;
    }

    public String getDateS() {

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(mDate);
        return s;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getPlace() {

        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }
}

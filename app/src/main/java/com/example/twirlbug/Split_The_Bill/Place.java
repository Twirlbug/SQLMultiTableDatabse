package com.example.twirlbug.Split_The_Bill;

import java.util.UUID;

/**
 * Created by Twirlbug on 3/16/2016.
 */
public class Place {
    private UUID pid;
    private String name;
    private String address;

    public Place() {
        this(UUID.randomUUID());}

    public Place(UUID id){
        pid = id;
        name = "";
        address = "" ;
    }

    public UUID getId(){
        return pid;
    }

    public String getName(){
        return name;
    }

    public void setName(String setname){
        name = setname;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String setAddress){
        address = setAddress;
    }


}



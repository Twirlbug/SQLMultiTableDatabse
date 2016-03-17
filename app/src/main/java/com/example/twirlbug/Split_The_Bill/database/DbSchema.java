package com.example.twirlbug.Split_The_Bill.database;

import android.provider.BaseColumns;

/**
 * Created by Nicole Geiger on 3/14/2016.
 */
public class DbSchema {

    public DbSchema()
    {

    }

    public static abstract class TableInfo implements BaseColumns
    {
        /* Initialize names for Database, Tables and the Columns of each tables
        *  3-14-2016 */
        public static final String DATABASE_NAME = "Split_the_Bill.db";
        public static final String Deal_Table = "Deal";
        public static final String Place_Table = "Place";
        public static final String Type_Table = "Business_Type";
        public static final String Purchase_Table = "Purchase";

        public static final class Deal {
            public static final String ID = "Deal_ID";
            public static final String UUID = "Deal_UUID";
            public static final String PID = "Deal_Place";
            public static final String DoD = "Date_of_Deal"; // Date and time stored as one varriable
            public static final String BTID = "Type_of_Deal";
        }

        public static final class Place {
            public static final String PID = "_id";
            public static final String PUUID = "Place_UUID";
            public static final String PN = "Place_Name";
            public static final String PA = "Place_Address";
        }

        public static final class Type {
            public static final String TID = "_id";
            public static final String TUUID = "Type_UUID";
            public static final String TN = "Type_Name";
        }

        public static final class Itemized_Purchase {
            public static final String PID = "Purchase_ID";
            public static final String PUUID = "Purchase_UUID";
            public static final String PC = "Purchase_Consumer";
            public static final String PB = "Purchase_Buyer";
            public static final String PP = "Purchase_Price";
            public static final String MID = "Purchase_in_Meal";
        }
    }
}

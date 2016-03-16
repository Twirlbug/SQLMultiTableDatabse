package com.example.twirlbug.Split_The_Bill.database;

import android.provider.BaseColumns;

/**
 * Created by Nicole Geiger on 3/14/2016.
 */
public class DbSchema {

    public DbSchema()
    {

    }

    public static abstract class TableInfo implements BaseColumns {
        /* Initialize names for Databse, Tables and the Columns of each tables
        *  3-14-2016 */
        public static final String DATABASE_NAME = "Split_the_Bill.db";
        public static final String Transaction_Table = "Meal";
        public static final String Place_Table = "Place";
        public static final String Type_Table = "Type";
        public static final String Purchase_Table = "Purchase";

        public static final class Trans {
            public static final String TrID = "Meal_ID";
            public static final String TrUUID = "Meal_UUID";
            public static final String TrRID = "Meal_Place";
            public static final String DoM = "Date_of_Meal"; // date and time are stored in one variable
            public static final String TrTID = "Type_of_Meal";
        }

        public static final class Place {
            public static final String PlID = "Place_ID";
            public static final String PlN = "Place_Name";
            public static final String PlA = "Place_Address";
        }

        public static final class Type {
            public static final String TID = "Type_ID";
            public static final String TN = "Type_Name";
        }

        public static final class Purchase {
            public static final String PID = "Purchase_ID";
            public static final String PC = "Purchase_Consumer";
            public static final String PB = "Purchase_Buyer";
            public static final String PP = "Purchase_Price";
            public static final String PMID = "Purchase_in_Meal";
        }
    }
}

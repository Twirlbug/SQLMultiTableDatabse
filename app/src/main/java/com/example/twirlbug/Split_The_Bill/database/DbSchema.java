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
        /* Initialize names for Databse, Tables and the Columns of each tables
        *  3-14-2016 */
        public static final String DATABASE_NAME = "Split_the_Bill.db";

        public static final String Meal_Table = "Meal";
        public static final String MID = "Meal_ID";
        public static final String MRID = "Meal_Resturant";
        public static final String DoM = "Date_of_Meal";
        public static final String ToM = "Time_of_Meal";
        public static final String MTID = "Type_of_Meal";

        public static final String Place_Table = "Place";
        public static final String RID = "Place_ID";
        public static final String RN = "Place_Name";
        public static final String RA = "Place_Address";

        public static final String Type_Table = "Type";
        public static final String TID = "Type_ID";
        public static final String TN = "Type_Name";

        public static final String Purchase_Table = "Purchase";
        public static final String PID = "Purchase_ID";
        public static final String PC = "Purchase_Consumer";
        public static final String PB = "Purchase_Buyer";
        public static final String PP = "Purchase_Price";
        public static final String PMID = "Purchase_in_Meal";
    }
}

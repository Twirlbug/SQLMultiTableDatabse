package com.example.twirlbug.Split_The_Bill;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.UUID;

/**
 * Created by Twirlbug on 3/16/2016.
 */
public class PlaceFragment extends Fragment {

    public static final String ARG_PLACE_ID = "place_id";


    private Place mPlace;

    EditText Place_Name;
    String getPlace_Name;

    EditText Place_Address;
    String getPlace_Address;
    Button place_submit;


    public static PlaceFragment newInstance(UUID placeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE_ID, placeId);

        PlaceFragment fragment = new PlaceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID placeID = (UUID) getArguments().getSerializable(ARG_PLACE_ID);
        this.setAddress(placeID);
        this.setName(placeID);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.place_insert_layout, container, false);

        Place_Name = (EditText) v.findViewById(R.id.Place_Name);
        Place_Address = (EditText) v.findViewById(R.id.Place_Address);
        place_submit = (Button) v.findViewById(R.id.Place_Button);


        return v;
    }



    private void setAddress(UUID placeId){
        SQLiteDatabase db = new DatabaseHelper(getContext()).getWritableDatabase();
        Cursor cursor = db.query(
                DbSchema.TableInfo.Place_Table,
                new String[]{DbSchema.TableInfo.Place.PN},
                DbSchema.TableInfo.Place.PUUID + " = ?",
                new String[]{placeId.toString()},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            mPlace.setAddress(cursor.getString(cursor.getColumnIndex(DbSchema.TableInfo.Place.PA)));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            mPlace.setAddress("");
        }
        cursor.close();

    }

    public void setName(UUID id){
        // Select Table ID where Table name is same as string name Query
        SQLiteDatabase db = new DatabaseHelper(getContext()).getWritableDatabase();
        Cursor cursor = db.query(
                DbSchema.TableInfo.Place_Table,
                new String[]{DbSchema.TableInfo.Place.PN},
                DbSchema.TableInfo.Place.PUUID + " = ?",
                new String[]{id.toString()},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            cursor.moveToFirst();
            mPlace.setName(cursor.getString(cursor.getColumnIndex(DbSchema.TableInfo.Place.PN)));
            //TypeName = "true";
        } else
        {
            // I AM EMPTY
            mPlace.setName("");
        }
        cursor.close();
    }

}

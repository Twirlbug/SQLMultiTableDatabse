package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;
import com.example.twirlbug.Split_The_Bill.database.DbSchema;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseFragment extends Fragment {

    private static final String ARG_PURCHASE_ID = "purchase_id";
    private static final String ARG_FROM_PURCHASE = "from_purchase";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Purchase mPurchase;
    private  UUID purchaseId;

    private Button mDateButton;
    private Button mTimeButton;
    private TextView mDatePlate;
    private TextView mPlaceShow;
    private Button mAddPlaceButton;
    private Button mEditPlace;
    private Button mAddTypeButton;
    private Button mEditType;
    private TextView mItemCount;
    private Button mAddItem;
    private Button mSubmitButton;
    private Button mDeleteButton;
    private Spinner mPlace_Spinner;
    private Spinner mType_Spinner;

    public static PurchaseFragment newInstance(UUID purchaseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PURCHASE_ID, purchaseId);

        PurchaseFragment fragment = new PurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause(){
        super.onPause();
        PurchaseLister.get(getActivity()).updatePurchase(mPurchase);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPurchase();
        upDatePlace();
        upDateTypes();
        Log.d("Changed Other tables", "Updated Purchase as well as as Types and Place spinner");


        //gets correct place to set place spinner
        String[] arrayForMatchingPlace = getPlacesInt();
        int idtofindplace = mPurchase.getPlace();
        int placesetting = 0;

        for (int i = 0; i <arrayForMatchingPlace.length; i++) {
            int db_type_id = Integer.parseInt(arrayForMatchingPlace[i]);
            if (idtofindplace == db_type_id) placesetting = i;
        }
        mPlace_Spinner.setSelection(placesetting);

        //gets correct place to set type spinner
        String[] arrayForMatching = getTypesInt();
        int idtofind = mPurchase.getType();
        int typesetting = 0;

        for (int i = 0; i <arrayForMatching.length; i++) {
            int db_type_id = Integer.parseInt(arrayForMatching[i]);
            if (idtofind == db_type_id) typesetting = i;
        }

        mType_Spinner.setSelection(typesetting);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchaseId = (UUID) getArguments().getSerializable(ARG_PURCHASE_ID);

        mPurchase = PurchaseLister.get(getActivity()).getPurchase(purchaseId);
        Log.d("OnCreate ", "Purchase created with type id " + mPurchase.getType() + " and Place id " + mPurchase.getPlace() );

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);

        //date and time show plate disable button functions
        mDatePlate = (TextView) v.findViewById(R.id.purchase_date_plate);

        //todo wire in widgets
        //date picker widget sets date
        mDateButton = (Button) v.findViewById(R.id.purchase_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mPurchase.getDate());
                dialog.setTargetFragment(PurchaseFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //Time button opens time picker and sets the time
        mTimeButton = (Button) v.findViewById(R.id.purchase_time);
        updateDate();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mPurchase.getDate());
                dialog.setTargetFragment(PurchaseFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        //Add Place Button Sends to add type page
        mAddPlaceButton = (Button) v.findViewById(R.id.add_place);
        mAddPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts and instance of the Add Place Activity
                Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                startActivity(intent);
            }
        });


        //EdiPlace Button Sends to Edit Place page
        mEditPlace = (Button) v.findViewById(R.id.edit_place);
        mEditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts and instance of the Edit Type Activity
                Intent intent = new Intent(getActivity(), EditPlaceActivity.class);
                Bundle c = new Bundle();
                c.putInt("id", mPurchase.getPlace());
                intent.putExtras(c);
                //Toast.makeText(getContext(), "Place id: " + mPurchase.getPlace(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //Add Type Button Sents to add type page
        mAddTypeButton = (Button) v.findViewById(R.id.add_type);
        mAddTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts and instance of the Add Type Activity
                Intent intent = new Intent(getActivity(), AddTypeActivity.class);
                startActivity(intent);
            }
        });

        //Edit Type Button Sends to edit type page
        mEditType = (Button) v.findViewById(R.id.edit_type);
        mEditType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts and instance of the Edit Type Activity
                Intent intent = new Intent(getActivity(), EditTypeActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", mPurchase.getType());
                intent.putExtras(b);
                //Toast.makeText(getContext(), "Type id: " + mPurchase.getType(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        mAddItem = (Button) v.findViewById(R.id.add_items);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ItemListActivity.class);
                Bundle b = new Bundle();
                UUID purchasedID = mPurchase.getID();
                DatabaseHelper dbh = new DatabaseHelper(getContext());
                b.putInt("purchase_id", dbh.TransToMainID(purchasedID));
                b.putInt(ARG_FROM_PURCHASE, 1);
                //Toast.makeText(getContext(),"Purchase id is " + dbh.TransToMainID(purchasedID), Toast.LENGTH_LONG).show() ;
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //Item textview shows the correct amount of items for this trasnaction
        mItemCount = (TextView) v.findViewById(R.id.item_count);
        int itemCount = 0;
        ItemLister itemLister = ItemLister.get(getActivity());
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        itemCount= itemLister.getItems(dbh.TransToMainID(mPurchase.getID())).size();
        String subtitle = getString(R.string.number_of_items, itemCount);
        mItemCount.setText(subtitle);


        //Submit Button Acts like hitting the back button and adding the purchase
        mSubmitButton = (Button) v.findViewById(R.id.submit_purchase);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //Delete Button deletes
        mDeleteButton = (Button) v.findViewById(R.id.delete_trans);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creates a dialog box to confirm deletion
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete Record");
                alert.setMessage("Are you sure you want to delete this transaction? WARNING: DOING SO WILL DELETE THIS TRANSACTION AND ANY ASSOCIATED ITEMS");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbh = new DatabaseHelper(getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        UUID id_of_purchase = mPurchase.getID();
                        int database_id_of_purchase = dbh.TransToMainID(id_of_purchase);
                        db.delete(DbSchema.TableInfo.Item_Table, DbSchema.TableInfo.Itemized_Purchase.MID + " = ?" , new String[] {Integer.toString(database_id_of_purchase)});;
                        db.delete(DbSchema.TableInfo.Deal_Table, DbSchema.TableInfo.Deal.UUID + " = ?", new String[] {id_of_purchase.toString()});
                        dialog.dismiss();
                        getActivity().onBackPressed();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        //show place Address
        mPlaceShow = (TextView) v.findViewById(R.id.Place_address);
        mPlaceShow.setText(dbh.PlaceToAddress((mPurchase.getPlace())));


        //Place Spinner Fills from Place Table Names
        mPlace_Spinner = (Spinner) v.findViewById(R.id.Place_Spinner_purchase);
        upDatePlace();

        String[] arrayForMatchingPlace = getPlacesInt();
        int idtofindplace = mPurchase.getPlace();
        int placesetting = 0;

        for (int i = 0; i <arrayForMatchingPlace.length; i++) {
            int db_type_id = Integer.parseInt(arrayForMatchingPlace[i]);
            if (idtofindplace == db_type_id) placesetting = i;
        }

        mPlace_Spinner.setSelection(placesetting);

        mPlace_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                int selection = mPlace_Spinner.getSelectedItemPosition();

                String[] arrayForMatching = getPlacesInt();
                int db_type = Integer.parseInt(arrayForMatching[selection]);

                mPurchase.setPlace(db_type);
                DatabaseHelper dbh = new DatabaseHelper(getContext());
                mPlaceShow.setText(dbh.PlaceToAddress((mPurchase.getPlace())));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        //Type Spinner Fills from Place Table Names
        mType_Spinner = (Spinner) v.findViewById(R.id.Type_Spinner_purchase);
        upDateTypes();

        String[] arrayForMatching = getTypesInt();
        int idtofind = mPurchase.getType();
        int typesetting = 0;

        for (int i = 0; i <arrayForMatching.length; i++) {
            int db_type_id = Integer.parseInt(arrayForMatching[i]);
            if (idtofind == db_type_id) typesetting = i;
        }

        mType_Spinner.setSelection(typesetting);

        mType_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                int selection = mType_Spinner.getSelectedItemPosition();

                String[] arrayForMatching = getTypesInt();
                int db_type = Integer.parseInt(arrayForMatching[selection]);

                mPurchase.setType(db_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            //use origin to keep time the same
            Date origin = mPurchase.getDate();

            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //set up calender to retrieve hour and minutes
            Calendar ori = Calendar.getInstance();
            ori.setTime(origin);
            int sethour = ori.get(Calendar.HOUR_OF_DAY);
            int setmin = ori.get(Calendar.MINUTE);

            //makes sure the time is as it was before
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, sethour);
            calendar.set(Calendar.MINUTE, setmin);
            date = calendar.getTime();

            mPurchase.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME) {
            //retrieve date for keeping day month year correct
            Date date = mPurchase.getDate();

            int sethour = (int) data.getSerializableExtra(TimePickerFragment.EXTRA_HOUR);
            int setmin = (int) data.getSerializableExtra(TimePickerFragment.EXTRA_MIN);

            //corrects the time is the full date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, sethour);
            calendar.set(Calendar.MINUTE, setmin);
            date = calendar.getTime();

            mPurchase.setDate(date);
            updateDate();

        }
    }

    private void updateDate() {
        mDatePlate.setText(mPurchase.getDate().toString());
    }

    private void upDateTypes() {
        String[] Types = getTypes();
        Log.d("OnCreate", "Update Types Length " + Types.length);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Types);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown); // Specify the layout to use when the list of choices appears
        mType_Spinner.setAdapter(adapter); // Apply the adapter to the spinner
    }

    public void upDatePlace() {
        String[] Places = getPlaces();
        Log.d("OnCreate", "Update Places Length " + Places.length);
        ArrayAdapter<String> adapterPlace = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Places);
        adapterPlace.setDropDownViewResource(R.layout.spinner_dropdown); // Specify the layout to use when the list of choices appears
        mPlace_Spinner.setAdapter(adapterPlace); // Apply the adapter to the spinner
    }

    public String[] getTypes() {
        Cursor cursor1 = new DatabaseHelper(getContext()).getWritableDatabase().rawQuery("SELECT " + DbSchema.TableInfo.Type.TN + " FROM " + DbSchema.TableInfo.Type_Table, null);
        cursor1.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor1.isAfterLast()) {
            names.add(cursor1.getString(cursor1.getColumnIndex(DbSchema.TableInfo.Type.TN)));
            cursor1.moveToNext();
        }
        cursor1.close();
        return names.toArray(new String[names.size()]);
    }

    public String[] getTypesInt() {
        Cursor cursor1 = new DatabaseHelper(getContext()).getWritableDatabase().rawQuery("SELECT " + DbSchema.TableInfo.Type.TID + " FROM " + DbSchema.TableInfo.Type_Table, null);
        cursor1.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor1.isAfterLast()) {
            names.add(cursor1.getString(cursor1.getColumnIndex(DbSchema.TableInfo.Type.TID)));
            cursor1.moveToNext();
        }
        cursor1.close();
        return names.toArray(new String[names.size()]);
    }


    public String[] getPlacesInt() {
        Cursor cursor1 = new DatabaseHelper(getContext()).getWritableDatabase().rawQuery("SELECT " + DbSchema.TableInfo.Place.PID + " FROM " + DbSchema.TableInfo.Place_Table, null);
        cursor1.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor1.isAfterLast()) {
            names.add(cursor1.getString(cursor1.getColumnIndex(DbSchema.TableInfo.Place.PID)));
            cursor1.moveToNext();
        }
        cursor1.close();
        return names.toArray(new String[names.size()]);
    }


    public String[] getPlaces() {
        Cursor cursor1 = new DatabaseHelper(getContext()).getWritableDatabase().rawQuery("SELECT " + DbSchema.TableInfo.Place.PN + " FROM " + DbSchema.TableInfo.Place_Table, null);
        cursor1.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while (!cursor1.isAfterLast()) {
            names.add(cursor1.getString(cursor1.getColumnIndex(DbSchema.TableInfo.Place.PN)));
            cursor1.moveToNext();
        }
        cursor1.close();
        return names.toArray(new String[names.size()]);
    }

    private void refreshPurchase() {
        mPurchase = PurchaseLister.get(getActivity()).getPurchase(purchaseId);
    }

}


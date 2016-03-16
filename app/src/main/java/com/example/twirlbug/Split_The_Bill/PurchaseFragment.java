package com.example.twirlbug.Split_The_Bill;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseFragment extends Fragment {

    private static final String ARG_PURCHASE_ID = "purchase_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Purchase mPurchase;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDatePlate;
    private Button mSubmitButton;
    private Spinner mType_Spinner;

    public static PurchaseFragment newInstance(UUID purchaseId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PURCHASE_ID, purchaseId);

        PurchaseFragment fragment = new PurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID purchaseId = (UUID) getArguments().getSerializable(ARG_PURCHASE_ID);

        mPurchase = PurchaseLister.get(getActivity()).getPurchase(purchaseId);

}

    @Override
    public void onPause() {
        super.onPause();

        PurchaseLister.get(getActivity())
                .updatePurchase(mPurchase);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);

        //date and time show plate disable button functions
        mDatePlate = (Button) v.findViewById(R.id.purchase_date_plate);
        mDatePlate.setEnabled(false);

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

        //Submit Button Acts like hitting the back button and adding the purchase
        mSubmitButton = (Button) v.findViewById(R.id.submit_purchase);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    getActivity().onBackPressed();
            }
        });


        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode!= Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
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

        if (requestCode == REQUEST_TIME){
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

    private void updateDate(){
        mDatePlate.setText(mPurchase.getDate().toString());
    }
}

package com.example.twirlbug.Split_The_Bill;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseFragment extends Fragment {

    private static final String ARG_PURCHASE_ID = "purchase_id";

    private Purchase mPurchase;
    private Button mDateButton;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);

        //todo wire in widgets
        //date widget
        mDateButton = (Button) v.findViewById(R.id.purchase_date);
        mDateButton.setText(mPurchase.getDate().toString());
        mDateButton.setEnabled(false);




        return v;
    }

 
}

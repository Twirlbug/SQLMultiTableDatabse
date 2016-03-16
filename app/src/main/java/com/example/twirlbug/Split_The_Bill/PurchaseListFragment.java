package com.example.twirlbug.Split_The_Bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Originally created by Nicole Geiger 9/7/2015
 * Note: Original was missing ALL comments due to import error
 * Edited on 3/15/2016 by Nicole Geiger
 */
public class PurchaseListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mPurchaseRecyclerView;
    private PurchaseAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);

        mPurchaseRecyclerView = (RecyclerView) view
                .findViewById(R.id.purchase_recycler_view);
        mPurchaseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState !=null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_purchase_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_purchase:
                Purchase purchase = new Purchase();
                PurchaseLister.get(getActivity()).addPurchase(purchase);
                Intent intent = PurchasePagerActivity
                        .newIntent(getActivity(), purchase.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        PurchaseLister purchaseLister = PurchaseLister.get(getActivity());
        int purchaseCount = purchaseLister.getPurchases().size();
        String subtitle = getString(R.string.subtitle_format, purchaseCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
        }

    private void updateUI() {
        PurchaseLister purchaselister = PurchaseLister.get(getActivity());
        List<Purchase> purchases = purchaselister.getPurchases();

        if (mAdapter == null) {
            mAdapter = new PurchaseAdapter(purchases);
            mPurchaseRecyclerView.setAdapter(mAdapter);
        }else{
            //mAdapter.setPurchases(purchases);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class PurchaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Purchase mPurchase;

        public TextView mDateTextView;
        private TextView mPlaceTextView;
        private TextView mTypeTextView;

        public PurchaseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // mDateTextView = (TextView) itemView; //working basic code for recycle viewer
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_date_text_view);
            mPlaceTextView = (TextView)
                    itemView.findViewById(R.id.list_item_location_text_view);
            mTypeTextView = (TextView)
                    itemView.findViewById(R.id.list_item_type_text_view);
        }

        public void bindPurchase(Purchase purchase){
            //bind the information to the list viewer
            mPurchase = purchase;
            mDateTextView.setText(mPurchase.getDateS());
            mPlaceTextView.setText(mPurchase.getPlace());
            mTypeTextView.setText(mPurchase.getType());

        }

        @Override
        public void onClick(View v){
            //testing to make sure on click is working in recycler view
            // Toast.makeText(getActivity(), mPurchase.getID().toString() +" clicked!" ,Toast.LENGTH_SHORT).show();

            //using an intent to pass in an extra: the ID of the selected Purchase
            Intent intent = PurchasePagerActivity.newIntent(getActivity(), mPurchase.getID());
            startActivity(intent);
        }
    }

    private class PurchaseAdapter extends RecyclerView.Adapter<PurchaseHolder> {
        private List<Purchase> mpurchase;

        public PurchaseAdapter(List<Purchase> purchase){
            mpurchase= purchase;
        }

        @Override
        public PurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    //.inflate(android.R.layout.simple_list_item_1, parent, false); //working basic code for recycle viewer
                    .inflate(R.layout.list_item_purchase, parent, false);
            return new PurchaseHolder(view);
        }

        @Override
        public void onBindViewHolder(PurchaseHolder holder, int position) {
            Purchase purchase = mpurchase.get(position);
            Log.d("Viewer Prepping", "Going to bind scroller at " + position); // for debugging purposes
            //holder.mDateTextView.setText(purchase.getDateS());  //working basic code for recycle viewer
            holder.bindPurchase(purchase);
        }

        @Override
        public int getItemCount() {
            return mpurchase.size();
        }


        public void setPurchases(List<Purchase> purchase) {
            mpurchase = purchase;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

}

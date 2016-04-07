package com.example.twirlbug.Split_The_Bill;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twirlbug.Split_The_Bill.database.DatabaseHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemListFragment extends Fragment {

    private static final String SAVED_ITEM_SUBTITLE_VISIBLE = "subtitle";
    //for from a purchase or from comparison 1 if purchase, 0 if comparison
    private static final String ARG_FROM_PURCHASE = "from_purchase";
    private int fromPurchase;

    //from purchase will have a purchase ID (fromPurchase == 1)
    private static final String ARG_PURCHASE_ID = "purchase_id";
    private int purchaseId;

    //from compare incoming variables
    private static final String PERSON_1 = "person_1";
    private static final String PERSON_2 = "person_2";
    private String person1name;
    private String person2name;
    

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private boolean mItemSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fromPurchase = getArguments().getInt(ARG_FROM_PURCHASE);
        if (fromPurchase ==1){ // this is from a purchase
            purchaseId = getArguments().getInt(ARG_PURCHASE_ID);
        } else if (fromPurchase == 0){ //this is from compare 2 people
            person1name = getArguments().getString(PERSON_1);
            person2name = getArguments().getString(PERSON_2);
        } else if (fromPurchase == 2) {//this is from the balance
            person1name = getArguments().getString(PERSON_1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItemRecyclerView = (RecyclerView) view
                .findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState !=null) {
            mItemSubtitleVisible = savedInstanceState.getBoolean(SAVED_ITEM_SUBTITLE_VISIBLE);
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
        if (fromPurchase == 1) { // item list view for from purchase
            inflater.inflate(R.menu.fragment_item_list_add_item, menu);
        } else { //item list view if from comparison
            inflater.inflate(R.menu.fragment_item_list, menu);
        }


        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);

        if(mItemSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        switch (items.getItemId()){
            case R.id.menu_item_new_item:
                //Toast.makeText(getContext(), "Add Item", Toast.LENGTH_SHORT).show();
                return addNewItem();
            case R.id.menu_item_new_summary:
                //Toast.makeText(getContext(), "Selected info", Toast.LENGTH_SHORT).show();
                fromPurchase = getArguments().getInt(ARG_FROM_PURCHASE);
                return accessSummaryFromPurchase(fromPurchase);
            case R.id.menu_item_show_subtitle:
                mItemSubtitleVisible = !mItemSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(items);
        }
    }

    private boolean addNewItem(){
        Item item = new Item();
        item.setmTransactionID(purchaseId);
        ItemLister.get(getActivity()).addItem(item);
        //Toast.makeText(getContext(), "Create New item", Toast.LENGTH_SHORT).show();
        //Intent intent = ItemPagerActivity
        //        .newIntent(getActivity(), item.getID(), fromPurchase, purchaseId);
        Intent intent = ItemActivity.newInstance(item.getID(), item.getmTransactionID(), getContext());
        //Toast.makeText(getContext(), "Started ItemPager", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        return true;
    }

    private boolean accessSummaryFromPurchase(int summarySelector){
        //selector 1 is from Purchase, selector 0 is from 2 person compare, selector 1 is for single person balance view
        Toast.makeText(getContext(), "Selected info Selector = " +summarySelector, Toast.LENGTH_SHORT).show();
        if (summarySelector == 1){
            Intent intentSummary = ItemSummary.newIntent(getActivity(), fromPurchase, purchaseId) ;
            startActivity(intentSummary);
            return true;
        }else if (summarySelector == 0){
            Intent intentSummary = ItemSummaryForCompare.newIntent(getActivity(),person1name, person2name) ;
            startActivity(intentSummary);
            return true;
        }else if (summarySelector == 2){
            Intent intentSummary = ItemSummaryForBalance.newIntent(getActivity(),person1name) ;
            startActivity(intentSummary);
            return true;
        }else return true;
    }


    private void updateSubtitle(){
        ItemLister itemLister = ItemLister.get(getActivity());
        int itemCount = 0;
        if (fromPurchase == 1){ //from a purchase
            itemCount= itemLister.getItems(purchaseId).size();
        }else if (fromPurchase == 0) { //from a compare 2 people
            itemCount = itemLister.getItems(person1name, person2name, getContext()).size();
        }else if (fromPurchase == 2){ //from a balance view
            itemCount = itemLister.getItems(person1name, getContext()).size();
        }
        //todo add in for comparisons for 1 person
        String subtitle = getString(R.string.subtitle_item_format, itemCount);

        if(!mItemSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        ItemLister itemlister = ItemLister.get(getActivity());
        List<Item> items = itemlister.getItems(); //.initalize to have all items
        if (fromPurchase == 1){ //from purchase
            items= itemlister.getItems(purchaseId);
        } else if (fromPurchase == 0) { // from comparison of 2
            items = itemlister.getItems(person1name, person2name, getContext());
        }else if (fromPurchase ==2) { //from a single balance
            items = itemlister.getItems(person1name, getContext());
        }
        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setItem(items);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Item mItem;

        private TextView mConsumerTextView;
        private TextView mBuyerTextView;
        private TextView mItemNameTextView;
        private TextView mItemCostTextView;
        private TextView mItemDate;
        private CheckBox mCheckBox;

        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // mDateTextView = (TextView) itemView; //working basic code for recycle viewer
            mConsumerTextView = (TextView)
                    itemView.findViewById(R.id.list_item_consumer_text_view);
            mBuyerTextView = (TextView)
                    itemView.findViewById(R.id.list_item_Buyer_text_view);
            mItemNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_name_text_view);
            mItemCostTextView = (TextView)
                    itemView.findViewById(R.id.money);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.gifted_checkbox);
            if(fromPurchase != 1) {//if not from a purchase also adds a date display
                mItemDate = (TextView) itemView.findViewById(R.id.date);
            }
        }

        public void bindItem(Item item) {
            //to handle receiving place and text as ints and change to name from Database
            DatabaseHelper db = new DatabaseHelper(getActivity());

            //bind the information to the list viewer
            mItem = item;
            mConsumerTextView.setText(mItem.getmConsumer());
            mBuyerTextView.setText(mItem.getmBuyer());
            mItemNameTextView.setText(mItem.getmName());
            mCheckBox.setChecked(mItem.getmGifted());
            mCheckBox.setEnabled(false);
            if (fromPurchase != 1){//if not from a purchase also adds a date display
                int transactionId = mItem.getmTransactionID();
                DatabaseHelper dbh = new DatabaseHelper(getContext());
                UUID transUUID = dbh.TransToUUID(transactionId);
                Purchase mPurchase = PurchaseLister.get(getActivity()).getPurchase(transUUID);
                mItemDate.setText(mPurchase.getDateS());

            }

            //making sure the price has two places after the decimal
            String dollar = Integer.toString(mItem.getmDollars());
            String cents = Integer.toString(mItem.getmCents());
            if (cents.length() < 2) {
                cents += 0;
                }

            mItemCostTextView.setText("$ " + dollar + "." + cents);

        }


        @Override
        public void onClick(View v){
            //testing to make sure on click is working in recycler view
            // Toast.makeText(getActivity(), mItem.getID().toString() +" clicked!" ,Toast.LENGTH_SHORT).show();

            //using an intent to pass in an extra: the ID of the selected Item
            if (fromPurchase ==1){ //if from a purchase
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getID(), fromPurchase, purchaseId);
            startActivity(intent);
            }
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mitem;

        public ItemAdapter(List<Item> item){
            mitem= item;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            if (fromPurchase == 1) {//if from a purchase shoe less view
                view = layoutInflater
                        //.inflate(android.R.layout.simple_list_item_1, parent, false); //working basic code for recycle viewer
                        .inflate(R.layout.list_item_item, parent, false);
        }else {view = layoutInflater
                    //.inflate(android.R.layout.simple_list_item_1, parent, false); //working basic code for recycle viewer
                    .inflate(R.layout.list_item_item_compare, parent, false);
            }
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mitem.get(position);
            //Log.d("Viewer Prepping", "Going to bind scroller"); // for debugging purposes
            //holder.mDateTextView.setText(item.getDateS());  //working basic code for recycle viewer
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mitem.size();
        }


        public void setItem(List<Item> item) {
            mitem = item;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_ITEM_SUBTITLE_VISIBLE, mItemSubtitleVisible);
    }
}

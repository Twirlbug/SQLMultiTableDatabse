package com.example.twirlbug.Split_The_Bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemPagerActivity extends AppCompatActivity{

    private static final String EXTRA_ITEM_ID = "com.example.twirlbug.Split_The_Bill.item_id";

    //for from a purchase or from comparison 1 if purchase, 0 if comparison
    private static final String ARG_FROM_PURCHASE = "from_purchase";
    private int fromPurchase;

    //from purchase will have a purchase ID (fromPurchase == 1)
    private static final String ARG_PURCHASE_ID = "purchase_id";
    private int purchaseId;


    private ViewPager mViewPager;
    private List<Item> mItem;

    public static Intent newIntent(Context packageContext, UUID itemId, int fromPurchase, int purchaseId){
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_ITEM_ID, itemId);
        b.putInt(ARG_FROM_PURCHASE, fromPurchase);
        b.putInt(ARG_PURCHASE_ID, purchaseId);
        intent.putExtras(b);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d("OnCreate", " Item Activity Called Create");
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        fromPurchase = bundle.getInt(ARG_FROM_PURCHASE);
        purchaseId = bundle.getInt(ARG_PURCHASE_ID);

        setContentView(R.layout.activity_item_pager);

        UUID itemId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);

        //Toast.makeText(getBaseContext(), "fromPurchase = " + fromPurchase, Toast.LENGTH_SHORT).show();

        if (fromPurchase == 1) {
            Log.d("Item Listed", "Selected List with purchaseID");
            mItem = ItemLister.get(this).getItems(purchaseId);
        }else {
            mItem = ItemLister.get(this).getItems();}

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Item item = mItem.get(position);
                //Toast.makeText(getBaseContext(), "Item opened", Toast.LENGTH_SHORT);
               if (fromPurchase == 1) {
                    return ItemFragment.newInstance(item.getID(), item.getmTransactionID());
                } else return ItemFragmentView.newInstance(item.getID(), item.getmTransactionID());

            }

            @Override
            public int getCount() {
                return mItem.size();
            }
        });

        for (int i =0; i< mItem.size(); i++){
            if(mItem.get(i).getID().equals(itemId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}


package com.example.twirlbug.Split_The_Bill;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewParent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/15/2016.
 */
public class PurchasePagerActivity extends AppCompatActivity {
    private static final String EXTRA_PURCHASE_ID = "com.example.twirlbug.Split_The_Bill.purchase_id";


    private ViewPager mViewPager;
    private List<Purchase> mPurchases;
    private boolean resume;

    public static Intent newIntent(Context packageContext, UUID purchaseId){
        Intent intent = new Intent(packageContext, PurchasePagerActivity.class);
        intent.putExtra(EXTRA_PURCHASE_ID, purchaseId);
        return intent;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (resume) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        resume = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_pager);

        UUID purchaseId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PURCHASE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_purchase_pager_view_pager);

        mPurchases = PurchaseLister.get(this).getPurchases();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Purchase purchase = mPurchases.get(position);
                resume = true;
                return PurchaseFragment.newInstance(purchase.getID());

            }

            @Override
            public int getCount() {
                return mPurchases.size();
            }
        });

        for (int i =0; i< mPurchases.size(); i++){
            if(mPurchases.get(i).getID().equals(purchaseId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


}

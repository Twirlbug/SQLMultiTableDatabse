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
import android.util.Log;
import android.view.ViewParent;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Twirlbug on 3/15/2016.
 */
public class PurchasePagerActivity extends AppCompatActivity {
    private static final String EXTRA_PURCHASE_ID = "com.example.twirlbug.Split_The_Bill.purchase_id";


    private boolean fromSave;
    private UUID  currentId;

    private ViewPager mViewPager;
    private List<Purchase> mPurchases;
    private UUID purchaseId;

    public static Intent newIntent(Context packageContext, UUID purchaseId){
        Intent intent = new Intent(packageContext, PurchasePagerActivity.class);
        intent.putExtra(EXTRA_PURCHASE_ID, purchaseId);
        return intent;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (fromSave) {
            Intent intent = new Intent(getBaseContext(), PurchasePagerActivity.class);
            intent.putExtra(EXTRA_PURCHASE_ID, currentId);
            finish();
            startActivity(intent);
            //mViewPager.setCurrentItem(position);
            //Toast.makeText(getBaseContext(), "Item position = " + position, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getBaseContext(), "Resumed Item position from saved " + fromSave, Toast.LENGTH_SHORT).show();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d("OnCreate", " Pager Activity Called Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_pager);
        fromSave = false;

        purchaseId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PURCHASE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_purchase_pager_view_pager);

        mPurchases = PurchaseLister.get(this).getPurchases();


        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {


            @Override
            public Fragment getItem(int position) {
                //Toast.makeText(getBaseContext(), "Purchase opened", Toast.LENGTH_SHORT);
                Purchase purchase = mPurchases.get(position);
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
                currentId = mPurchases.get(i).getID();
                break;
            }
        }

    }

@Override
public void onPause(){
    super.onPause();
    fromSave=true;
    currentId = mPurchases.get(mViewPager.getCurrentItem()).getID();
    //Toast.makeText(getBaseContext(), "Paused Item position " , Toast.LENGTH_SHORT).show();
}


}

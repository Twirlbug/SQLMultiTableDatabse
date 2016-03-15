package com.example.twirlbug.Split_The_Bill;

import android.support.v4.app.Fragment;

/**
 * Created by Twirlbug on 3/14/2016.
 */
public class PurchaseListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PurchaseListFragment();
    }
}

package com.example.twirlbug.Split_The_Bill;

import android.support.v4.app.Fragment;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        Fragment forArgPassing = new ItemListFragment();
        forArgPassing.setArguments(getIntent().getExtras());
        return forArgPassing;
    }
}
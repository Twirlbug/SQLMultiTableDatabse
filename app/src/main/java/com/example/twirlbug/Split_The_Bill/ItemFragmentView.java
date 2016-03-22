package com.example.twirlbug.Split_The_Bill;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Twirlbug on 3/19/2016.
 */
public class ItemFragmentView extends Fragment {

        private static final String ARG_ITEM_ID = "item_id";
        private static final String ARG_TRANS_ID = "transaction_id";

        private Item mItem;
        private UUID itemId;
        private int purchaseID;


        @Override
        public void onPause(){
            super.onPause();
            ItemLister.get(getActivity()).updateItem(mItem);
        }

        public static ItemFragmentView newInstance(UUID itemId, int purchaseID) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_ITEM_ID, itemId);
            args.putInt(ARG_TRANS_ID, purchaseID);
            ItemFragmentView fragment = new ItemFragmentView();
            fragment.setArguments(args);
            return fragment;
        }


}

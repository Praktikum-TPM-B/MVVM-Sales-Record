package com.maulanakurnia.salesrecord.ui.adapter;

import android.view.View;

import com.maulanakurnia.salesrecord.data.model.SalesRecord;

/**
 * Created by Maulana Kurnia on 5/30/2021
 * Keep Coding & Stay Awesome!
 **/
public interface AdapterImpl {
    interface OnItemLongClickListener {
        void onItemClick(SalesRecord salesRecord);
    }

    interface OnItemClickListener {
        void onItemClick(View view);
    }
}

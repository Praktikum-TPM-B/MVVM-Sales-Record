package com.maulanakurnia.salesrecord.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordViewModel;
import com.maulanakurnia.salesrecord.ui.adapter.SalesRecordAdapter;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.util.Date;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
public class MainActivity extends AppCompatActivity {
    public static final String SALES_RECORD_ID = "SALES_RECORD_ID";

    private RecyclerView recyclerView;
    private SalesRecordAdapter salesRecordAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_sales_record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SalesRecordViewModel salesRecordViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this
                        .getApplication())
                .create(SalesRecordViewModel.class);

        salesRecordViewModel.getAll().observe(this, salesRecords -> {
            salesRecordAdapter = new SalesRecordAdapter(salesRecords, MainActivity.this);
            recyclerView.setAdapter(salesRecordAdapter);
        });

        FloatingActionButton addRecord = findViewById(R.id.fb_add);
        addRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormActivity.class);
            startActivity(intent);
        });
    }
}

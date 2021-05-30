package com.maulanakurnia.salesrecord.ui.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordViewModel;
import com.maulanakurnia.salesrecord.ui.adapter.SalesRecordAdapter;
import com.maulanakurnia.salesrecord.utils.BetterActivityResult;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
public class MainActivity extends AppCompatActivity {

    public static final int ADD_SALES_RECORD_REQUEST = 1;
    public static final int EDIT_SALES_RECORD_REQUEST = 2;

    private View bsView;
    private AtomicBoolean isPressed;
    private SimpleDateFormat dateFormat;
    private BottomSheetDialog bottomSheet;
    private MaterialAlertDialogBuilder builder;
    private LinearLayout change, delete, cancel;
    private SalesRecordViewModel salesRecordViewModel;

    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv_sales_record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final SalesRecordAdapter salesRecordAdapter  = new SalesRecordAdapter();
        recyclerView.setAdapter(salesRecordAdapter);

        isPressed   = new AtomicBoolean(false);
        bottomSheet = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        builder     = new MaterialAlertDialogBuilder(this, R.layout.dialog_question);
        dateFormat  = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
        bsView      = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,null);

        salesRecordViewModel = new ViewModelProvider(this).get(SalesRecordViewModel.class);
        salesRecordViewModel.getAll().observe(this, salesRecordAdapter::submitList);
        FloatingActionButton addRecord = findViewById(R.id.fb_add);

        addRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormActivity.class);
            openSomeActivityForResult(intent);
        });

        salesRecordAdapter.setOnItemClickListener(v -> {
            ConstraintLayout salesDetail = v.findViewById(R.id.card_detail);
            if(!isPressed.get()){
                salesDetail.setVisibility(VISIBLE);
                isPressed.set(true);
            } else {
                salesDetail.setVisibility(GONE);
                isPressed.set(false);
            }
        });

        salesRecordAdapter.setOnItemLongClickListener(salesRecord -> {
            change = bsView.findViewById(R.id.llChange);
            delete = bsView.findViewById(R.id.llDelete);
            cancel = bsView.findViewById(R.id.llCancel);

            change.setOnClickListener(v21 -> {
                Intent intent = new Intent(this, FormActivity.class);
                intent.putExtra(FormActivity.EXTRA_ID, salesRecord.getId());
                bottomSheet.dismiss();
                openSomeActivityForResult(intent);
            });

            delete.setOnClickListener(v22 -> {
                bottomSheet.dismiss();
                View dialog             = LayoutInflater.from(this).inflate(R.layout.dialog_question, findViewById(R.id.layout_dialog));
                builder.setView(dialog);
                AlertDialog alertDialog = builder.create();

                dialog.findViewById(R.id.dialog_action_cancel).setOnClickListener(v3 -> {
                    alertDialog.dismiss();
                    bottomSheet.show();
                });

                dialog.findViewById(R.id.dialog_action_delete).setOnClickListener(v4 -> {
                    salesRecordViewModel.delete(salesRecord.getId());
                    alertDialog.dismiss();
                    bottomSheet.dismiss();
                    Toast.makeText(this, "Berhasil menghapus catatan "+dateFormat.format(salesRecord.getDate()), Toast.LENGTH_SHORT).show();
                });

                if(alertDialog.getWindow() != null) { alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }

                alertDialog.show();
            });

            cancel.setOnClickListener(v23 -> {
                bottomSheet.dismiss();
            });

            bottomSheet.setContentView(bsView);
            bottomSheet.show();
        });
    }

    public void openSomeActivityForResult(Intent intent) {
        activityLauncher.launch(intent, result -> {
            int getResult = result.getResultCode();

            if(getResult == ADD_SALES_RECORD_REQUEST || getResult == EDIT_SALES_RECORD_REQUEST) {
                assert result.getData() != null;
                Long date           = result.getData().getLongExtra(FormActivity.EXTRA_DATE, 0);
                Double gross_provit = result.getData().getDoubleExtra(FormActivity.EXTRA_GROSS_PROVIT, 0);
                Double expenditure  = result.getData().getDoubleExtra(FormActivity.EXTRA_EXPENDITURE, 0);
                Double net_gross    = result.getData().getDoubleExtra(FormActivity.EXTRA_NET_GROSS,0);
                SalesRecord salesRecord = new SalesRecord();
                if(getResult == ADD_SALES_RECORD_REQUEST) {

                    salesRecord.setDate(DateTypeConverter.toDate(date));
                    salesRecord.setGross_profit(gross_provit);
                    salesRecord.setExpenditure(expenditure);
                    salesRecord.setNet_gross(net_gross);
                    salesRecordViewModel.insert(salesRecord);
                    Toast.makeText(MainActivity.this, "Data berhasil disimpan!",Toast.LENGTH_SHORT).show();

                } else {
                    int id = result.getData().getIntExtra(FormActivity.EXTRA_ID, -1);
                    if(id == -1) {
                        Toast.makeText(MainActivity.this, "Data tidak dapat diperbaharui", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    salesRecord.setId(id);
                    salesRecord.setDate(DateTypeConverter.toDate(date));
                    salesRecord.setGross_profit(gross_provit);
                    salesRecord.setExpenditure(expenditure);
                    salesRecord.setNet_gross(net_gross);
                    salesRecordViewModel.update(salesRecord);
                    Toast.makeText(MainActivity.this, "Data berhasil diperbaharui",Toast.LENGTH_SHORT).show();
                }
            }
             else {
                Toast.makeText(MainActivity.this, "Aksi Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

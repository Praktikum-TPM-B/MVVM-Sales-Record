package com.maulanakurnia.salesrecord.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordViewModel;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;
import com.maulanakurnia.salesrecord.utils.Currency;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
public class FormActivity extends AppCompatActivity {

    private int salesRecordID    = 0;
    private boolean isEdit       = false;

    private EditText dateInput, grossprovitInput, expenditureInput;
    private Button submit;
    private TextView title;
    protected LinearLayout back;
    protected SalesRecordViewModel salesRecordViewModel;
    protected SimpleDateFormat dateFormat;
    protected MaterialDatePicker<Long> datePicker;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        dateInput               = findViewById(R.id.et_date);
        grossprovitInput        = findViewById(R.id.et_gross_provit);
        expenditureInput        = findViewById(R.id.et_expenditure);
        submit                  = findViewById(R.id.btn_submit);
        title                   = findViewById(R.id.form_title);
        back                    = findViewById(R.id.back);
        salesRecordViewModel    = new ViewModelProvider.AndroidViewModelFactory(FormActivity.this.getApplication()).create(SalesRecordViewModel.class);
        dateFormat              = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));


        datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih Tanggal")
                    .setCalendarConstraints(new CalendarConstraints.Builder().setOpenAt(Calendar.getInstance().getTime().getTime()).build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

        if(!isEdit) {
            datePicker.show(getSupportFragmentManager(), datePicker.getTag());
            datePicker.dismiss();
            dateInput.setText(dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));
        }

        dateInput.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), datePicker.getTag());
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            dateInput.setText(dateFormat.format(datePicker.getSelection()));
        });

        grossprovitInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Currency.input(grossprovitInput, this);
            }
        });

        expenditureInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Currency.input(expenditureInput, this);
            }
        });

        if(getIntent().hasExtra(MainActivity.SALES_RECORD_ID)) {
            salesRecordID = getIntent().getIntExtra(MainActivity.SALES_RECORD_ID,0);

            salesRecordViewModel.get(salesRecordID).observe(this, salesRecord -> {
                if(salesRecord != null) {
                    datePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Pilih Tanggal")
                            .setCalendarConstraints(new CalendarConstraints.Builder().setOpenAt(salesRecord.getDate().getTime()).build())
                            .setSelection(DateTypeConverter.fromDate(salesRecord.getDate())).build();
                    datePicker.show(getSupportFragmentManager(), datePicker.getTag());
                    datePicker.dismiss();
                    dateInput.setText(dateFormat.format(salesRecord.getDate()));
                    grossprovitInput.setText(salesRecord.getGross_profit().toString());
                    expenditureInput.setText(salesRecord.getExpenditure().toString());

                    isEdit = true;
                    submit.setText("UBAH");
                    title.setText("Ubah Catatan");
                }
            });
        }

        submit.setOnClickListener(v -> {
            int id                  = salesRecordID;
            String grossProfit      = grossprovitInput.getText().toString();
            String expenditure      = expenditureInput.getText().toString();
            String netGross         = String.valueOf(Double.parseDouble(Currency.trimComma(grossProfit)) - Double.parseDouble(Currency.trimComma(expenditure)));
            SalesRecord salesRecord = new SalesRecord();
            if(!isEdit) {
                try {
                    salesRecord.setDate(DateTypeConverter.toDate(datePicker.getSelection()));
                    salesRecord.setExpenditure(Double.parseDouble(Currency.trimComma(expenditure)));
                    salesRecord.setGross_profit(Double.parseDouble(Currency.trimComma(grossProfit)));
                    salesRecord.setNet_gross(Double.parseDouble(Currency.trimComma(netGross)));
                    SalesRecordViewModel.insert(salesRecord);
                    finish();
                }catch (Exception err) {
                    Log.i("EXCEPTION", "ERROR: "+ err);
                }
            }
            else {
                try{
                    salesRecord.setId(id);
                    salesRecord.setDate(DateTypeConverter.toDate((Long) datePicker.getSelection()));
                    salesRecord.setGross_profit(Double.parseDouble(Currency.trimComma(grossProfit)));
                    salesRecord.setExpenditure(Double.parseDouble(Currency.trimComma(expenditure)));
                    salesRecord.setNet_gross(Double.parseDouble(netGross));
                    SalesRecordViewModel.update(salesRecord);
                    isEdit = false;
                    finish();
                }catch (NullPointerException err) {
                    Log.i("EXCETION", "ERRR: "+err);
                }
            }
        });

        back.setOnClickListener(v->finish());
    }
}

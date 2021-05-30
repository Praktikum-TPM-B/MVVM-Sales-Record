package com.maulanakurnia.salesrecord.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordViewModel;
import com.maulanakurnia.salesrecord.utils.Currency;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
public class FormActivity extends AppCompatActivity {

    public static final String EXTRA_ID             = "EXTRA_ID";
    public static final String EXTRA_DATE           = "EXTRA_DATE";
    public static final String EXTRA_GROSS_PROVIT   = "EXTRA_GROSS_PROVIT";
    public static final String EXTRA_EXPENDITURE    = "EXTRA_EXPENDITURE";
    public static final String EXTRA_NET_GROSS      = "EXTRA_NET_GROSS";

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
        datePicker              = MaterialDatePicker.Builder.datePicker()
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
            if(datePicker.isAdded()) {
                datePicker.dismiss();
            }
            datePicker.show(getSupportFragmentManager(), datePicker.getTag());
        });

        datePicker.addOnPositiveButtonClickListener(selection -> dateInput.setText(dateFormat.format(datePicker.getSelection())));

        grossprovitInput.addTextChangedListener(new Currency(grossprovitInput));
        expenditureInput.addTextChangedListener(new Currency(expenditureInput));

        if(getIntent().hasExtra(EXTRA_ID)) {
            salesRecordID = getIntent().getIntExtra(EXTRA_ID,-1);

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
            String grossProfit      = grossprovitInput.getText().toString();
            String expenditure      = expenditureInput.getText().toString();
            String netGross         = String.valueOf(Double.parseDouble(Currency.trimComma(grossProfit)) - Double.parseDouble(Currency.trimComma(expenditure)));

            Intent data = new Intent();
            data.putExtra(EXTRA_DATE, datePicker.getSelection());
            data.putExtra(EXTRA_GROSS_PROVIT, Double.parseDouble(Currency.trimComma(grossProfit)));
            data.putExtra(EXTRA_EXPENDITURE, Double.parseDouble(Currency.trimComma(expenditure)));
            data.putExtra(EXTRA_NET_GROSS, Double.parseDouble(Currency.trimComma(netGross)));

            salesRecordID = getIntent().getIntExtra(EXTRA_ID, -1);
            if (salesRecordID != -1) {
                data.putExtra(EXTRA_ID, salesRecordID);
                setResult(MainActivity.EDIT_SALES_RECORD_REQUEST, data);
            }else {
                setResult(MainActivity.ADD_SALES_RECORD_REQUEST, data);
            }
            finish();

        });

        back.setOnClickListener(v->finish());
    }
}

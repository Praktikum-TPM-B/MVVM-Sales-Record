package com.maulanakurnia.salesrecord.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordViewModel;
import com.maulanakurnia.salesrecord.ui.view.FormActivity;
import com.maulanakurnia.salesrecord.ui.view.MainActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Maulana Kurnia on 5/27/2021
 * Keep Coding & Stay Awesome!
 **/
public class SalesRecordAdapter extends RecyclerView.Adapter<SalesRecordAdapter.ViewHolder> {

    private final List<SalesRecord> salesRecords;
    private final Context context;
    private LinearLayout change, delete, cancel;

    public SalesRecordAdapter(List<SalesRecord> salesRecords, Context context) {
        this.salesRecords = salesRecords;
        this.context = context;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @SuppressLint({"SetTextI18n","InflateParams"}) @Override
    public void onBindViewHolder(@NonNull SalesRecordAdapter.ViewHolder holder, int position) {
        AtomicBoolean isPressed                 = new AtomicBoolean(false);
        SimpleDateFormat dayFormat              = new SimpleDateFormat("EEEE", new Locale("id","ID"));
        SimpleDateFormat dateFormat             = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
        BottomSheetDialog bottomSheet           = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View view                               = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog,null);
        MaterialAlertDialogBuilder builder      = new MaterialAlertDialogBuilder(context, R.layout.dialog_question);
        SalesRecord salesRecord                 = salesRecords.get(position);

        holder.cardView.setOnClickListener(v -> {
            ConstraintLayout salesDetail = v.findViewById(R.id.card_detail);
            if(!isPressed.get()){
                salesDetail.setVisibility(View.VISIBLE);
                isPressed.set(true);
            } else {
                salesDetail.setVisibility(View.GONE);
                isPressed.set(false);
            }
        });

        holder.cardView.setOnLongClickListener(v2 -> {
            change = view.findViewById(R.id.llChange);
            delete = view.findViewById(R.id.llDelete);
            cancel = view.findViewById(R.id.llCancel);

            change.setOnClickListener(v21 -> {
                Intent intent = new Intent(context, FormActivity.class);
                intent.putExtra(MainActivity.SALES_RECORD_ID, salesRecord.getId());
                bottomSheet.dismiss();
                context.startActivity(intent);
            });

            delete.setOnClickListener(v22 -> {
                bottomSheet.dismiss();

                View dialog             = LayoutInflater.from(context).inflate(R.layout.dialog_question, view.findViewById(R.id.layout_dialog));
                builder.setView(dialog);
                AlertDialog alertDialog = builder.create();

                dialog.findViewById(R.id.dialog_action_cancel).setOnClickListener(v3 -> {
                    alertDialog.dismiss();
                    bottomSheet.show();
                });

                dialog.findViewById(R.id.dialog_action_delete).setOnClickListener(v4 -> {
                    SalesRecordViewModel.delete(salesRecord.getId());
                    alertDialog.dismiss();
                    bottomSheet.dismiss();
                    Toast.makeText(context, "Berhasil menghapus catatan "+dateFormat.format(salesRecord.getDate()), Toast.LENGTH_SHORT).show();
                });

                if(alertDialog.getWindow() != null) { alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }

                alertDialog.show();
            });

            cancel.setOnClickListener(v23 -> {
                bottomSheet.dismiss();
            });

            bottomSheet.setContentView(view);
            bottomSheet.show();
            return true;
        });

        holder.day.setText(dayFormat.format(salesRecord.getDate()));

        DecimalFormat indonesianExchangeRate = (DecimalFormat)
            DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            indonesianExchangeRate.setDecimalFormatSymbols(formatRp);

        holder.date.setText(dateFormat.format(salesRecord.getDate()));
        holder.gross_provit.setText(indonesianExchangeRate.format(Double.parseDouble(salesRecord.getGross_profit().toString())));
        holder.expenditure.setText(indonesianExchangeRate.format(Double.parseDouble(salesRecord.getExpenditure().toString())));
        holder.net_gross.setText(indonesianExchangeRate.format(Double.parseDouble(salesRecord.getNet_gross().toString())));

        if(salesRecord.getNet_gross() < 0) {
            holder.net_gross.setTextColor(Color.parseColor("#E83737"));
            holder.ic_net_gross.setImageResource(R.drawable.ic_arrow_down);
        }else if(salesRecord.getNet_gross() > 0){
            holder.net_gross.setTextColor(Color.parseColor("#098C0A"));
            holder.ic_net_gross.setImageResource(R.drawable.ic_arrow_up);
        } else {
            holder.net_gross.setTextColor(Color.parseColor("#313131"));
            holder.ic_net_gross.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return salesRecords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day, date, gross_provit, expenditure, net_gross;
        public CardView cardView;
        public ImageView ic_net_gross;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day             = itemView.findViewById(R.id.txt_day);
            date            = itemView.findViewById(R.id.txt_date);
            gross_provit    = itemView.findViewById(R.id.txt_gross_provit);
            expenditure     = itemView.findViewById(R.id.txt_expenditure);
            net_gross       = itemView.findViewById(R.id.txt_net_gross);
            cardView        = itemView.findViewById(R.id.card_sales_record);
            ic_net_gross    = itemView.findViewById(R.id.icon_net_gross);
        }
    }
}

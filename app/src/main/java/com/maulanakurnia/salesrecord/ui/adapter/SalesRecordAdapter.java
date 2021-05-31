package com.maulanakurnia.salesrecord.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Maulana Kurnia on 5/27/2021
 * Keep Coding & Stay Awesome!
 **/
public class SalesRecordAdapter extends ListAdapter<SalesRecord, SalesRecordAdapter.ViewHolder> {

    private AdapterImpl.OnItemClickListener clickListener;
    private AdapterImpl.OnItemLongClickListener longClickListener;

    private static final DiffUtil.ItemCallback<SalesRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<SalesRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull SalesRecord oldItem, @NonNull SalesRecord newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SalesRecord oldItem, @NonNull SalesRecord newItem) {
            return oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getGross_profit().equals(newItem.getGross_profit()) &&
                    oldItem.getExpenditure().equals(newItem.getExpenditure()) &&
                    oldItem.getNet_gross().equals(newItem.getNet_gross());
        }
    };

    public SalesRecordAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n","InflateParams"}) @Override
    public void onBindViewHolder(@NonNull SalesRecordAdapter.ViewHolder holder, int position) {
        SalesRecord salesRecord                 = getItem(position);

        SimpleDateFormat dayFormat              = new SimpleDateFormat("EEEE", new Locale("id","ID"));
        SimpleDateFormat dateFormat             = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
        
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
            holder.net_gross.setTextColor(Color.parseColor("#C5221F"));
            holder.container_logo.setCardBackgroundColor(Color.parseColor("#FCE8E6"));
            holder.logo_info.setImageResource(R.drawable.ic_baseline_arrow_downward);
            holder.logo_info.setColorFilter(Color.parseColor("#C5221F"));

        }else if(salesRecord.getNet_gross() > 0){
            holder.net_gross.setTextColor(Color.parseColor("#098C0A"));
            holder.container_logo.setCardBackgroundColor(Color.parseColor("#E6F4EA"));
            holder.logo_info.setImageResource(R.drawable.ic_baseline_arrow_upward);
            holder.logo_info.setColorFilter(Color.parseColor("#098C0A"));
        } else {
            holder.net_gross.setTextColor(Color.parseColor("#0080FF"));
            holder.container_logo.setCardBackgroundColor(Color.parseColor("#E8F0FE"));
            holder.logo_info.setImageResource(R.drawable.ic_baseline_unfold_less);
            holder.logo_info.setColorFilter(Color.parseColor("#0080FF"));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day, date, gross_provit, expenditure, net_gross;
        public CardView cardView, container_logo;
        public ImageView logo_info;
        public View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day             = itemView.findViewById(R.id.txt_day);
            date            = itemView.findViewById(R.id.txt_date);
            gross_provit    = itemView.findViewById(R.id.txt_gross_provit);
            expenditure     = itemView.findViewById(R.id.txt_expenditure);
            net_gross       = itemView.findViewById(R.id.txt_net_gross);
            cardView        = itemView.findViewById(R.id.card_sales_record);
            logo_info       = itemView.findViewById(R.id.logo_info);
            container_logo  = itemView.findViewById(R.id.container_logo);
            divider         = itemView.findViewById(R.id.divider);

            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(cardView);
                }
            });

            cardView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemClick(getItem(position));
                }
                return true;
            });
        }
    }

    public void setOnItemClickListener(AdapterImpl.OnItemClickListener l) {
        this.clickListener = l;
    }

    public void setOnItemLongClickListener(AdapterImpl.OnItemLongClickListener l) {
        this.longClickListener = l;
    }

}

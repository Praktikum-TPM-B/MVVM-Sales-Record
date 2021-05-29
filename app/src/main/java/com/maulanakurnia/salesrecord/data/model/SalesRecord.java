package com.maulanakurnia.salesrecord.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.util.Date;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
@Entity(tableName = "sales_record")
public class SalesRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters(DateTypeConverter.class)
    private Date date;
    private Double gross_profit;
    private Double expenditure;
    private Double net_gross;


    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Double getGross_profit() {
        return gross_profit;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public Double getNet_gross() {
        return net_gross;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGross_profit(Double gross_profit) {
        this.gross_profit = gross_profit;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public void setNet_gross(Double net_gross) {
        this.net_gross = net_gross;
    }
}

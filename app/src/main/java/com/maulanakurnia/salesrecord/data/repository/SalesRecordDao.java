package com.maulanakurnia.salesrecord.data.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
@Dao
public interface SalesRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SalesRecord salesRecord);

    @Query("SELECT * FROM sales_record ORDER BY date ASC")
    LiveData<List<SalesRecord>> getAll();

    @Query("SELECT * FROM sales_record WHERE id == :id")
    LiveData<SalesRecord> get(int id);

    @Update
    void update(SalesRecord salesRecord);

    @Query("DELETE FROM sales_record WHERE id = :id")
    void deleteById(long id);

    @Query("DELETE FROM sales_record")
    void deleteAll();
}

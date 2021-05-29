package com.maulanakurnia.salesrecord.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
public class SalesRecordViewModel extends AndroidViewModel {

    public SalesRecordRepository repository;
    public final LiveData<List<SalesRecord>> listSalesRecord;

    public SalesRecordViewModel(@NonNull Application application) {
        super(application);

        repository  = new SalesRecordRepository(application);
        listSalesRecord = repository.getAllData();
    }

    public LiveData<List<SalesRecord>> getAll() {
        return listSalesRecord;
    }

    public void insert(SalesRecord salesRecord) {
        repository.insert(salesRecord);
    }

    public LiveData<SalesRecord> get(int id) {
        return repository.get(id);
    }

    public void update(SalesRecord salesRecord) {
        repository.update(salesRecord);
    }

    public void delete(long id) {
        repository.delete(id);
    }
}

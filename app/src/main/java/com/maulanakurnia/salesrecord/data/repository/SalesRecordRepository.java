package com.maulanakurnia.salesrecord.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
class SalesRecordRepository {

    private SalesRecordDao salesRecordDAO;
    private LiveData<List<SalesRecord>> listContact;

    public SalesRecordRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        salesRecordDAO = db.salesRecordDao();
        listContact = salesRecordDAO.getAll();
    }

    public LiveData<List<SalesRecord>> getAllData() {
        return listContact;
    }

    public void insert(SalesRecord salesRecord) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            salesRecordDAO.insert(salesRecord);
        });
    }

    public LiveData<SalesRecord> get(int id) {
        return salesRecordDAO.get(id);
    }

    public void update(SalesRecord contact) {
        AppDatabase.databaseWriteExecutor.execute(()->
                salesRecordDAO.update(contact));
    }

    public void delete(long id) {
        AppDatabase.databaseWriteExecutor.execute(()->
                salesRecordDAO.deleteById(id));
    }
}

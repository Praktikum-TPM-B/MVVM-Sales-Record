package com.maulanakurnia.salesrecord.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.data.repository.SalesRecordDao;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Maulana Kurnia on 5/28/2021
 * Keep Coding & Stay Awesome!
 **/
@Database(entities = {SalesRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SalesRecordDao salesRecordDao();
    public static final int NUMBER_OF_THREADS = 4;
    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "salesrecord.db")
                        .addCallback(sRoomDatabaseCallback)
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return INSTANCE;
    }

    // This method will be invoked in when app build database in the getDatabase method
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // populate the database on the background
            databaseWriteExecutor.execute(() -> {
                SalesRecordDao salesRecordDAO = INSTANCE.salesRecordDao();
                salesRecordDAO.deleteAll();

                // Insert sales record
                SalesRecord salesRecord = new SalesRecord();
                salesRecord.setDate(Calendar.getInstance().getTime());
                salesRecord.setExpenditure(2000000.0);
                salesRecord.setGross_profit(2500000.0);
                salesRecord.setNet_gross(600000.00);
                salesRecordDAO.insert(salesRecord);
            });
        }
    };
}

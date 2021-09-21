package com.example.demolauncher.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.demolauncher.room.HaertRate.HeartRateDao;
import com.example.demolauncher.room.HaertRate.HeartRateData;

@Database(entities = {HeartRateData.class } , version = 1)
public abstract class LauncherDatabase extends RoomDatabase {
    public abstract HeartRateDao heartRateDao();

//    public static final Migration MIGRATION_1_2 = new Migration(1 , 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE BloodPressureData ADD COLUMN" +
//                    "timeInMillis INTEGER NOT NULL DEFAULT 1305213");
//            database.execSQL("ALTER TABLE HeartRateData ADD COLUMN heartRate INTEGER NOT NULL DEFAULT 86");
//        }
//    };
}

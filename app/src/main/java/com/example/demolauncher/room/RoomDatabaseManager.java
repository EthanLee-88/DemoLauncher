package com.example.demolauncher.room;

import android.content.Context;

import androidx.room.Room;

import com.example.demolauncher.application.BaseApplication;
import com.example.demolauncher.room.HaertRate.HeartRateDao;
import com.example.demolauncher.room.HaertRate.HeartRateData;
import com.example.demolauncher.util.AppExecutors;

public class RoomDatabaseManager {
    private static RoomDatabaseManager roomDatabaseManager;
    private LauncherDatabase mRoomDatabase;
    private Context mContext = BaseApplication.applicationContext;
    public String currentAccount = "" ;//

    private RoomDatabaseManager(){}

    public synchronized static RoomDatabaseManager getInstance(){
        if (roomDatabaseManager == null){
            synchronized (RoomDatabaseManager.class){
                if (roomDatabaseManager == null){
                    roomDatabaseManager = new RoomDatabaseManager();
                }
            }
        }
        return roomDatabaseManager;
    }

    public synchronized LauncherDatabase getDatabase(){
        if (mRoomDatabase == null){
                    if (mContext != null){
                        mRoomDatabase = Room.databaseBuilder(mContext ,
                                LauncherDatabase.class , "JY02_medical_data")
                                .fallbackToDestructiveMigration() //升级数据库，清除数据库原有数据
                                .allowMainThreadQueries()   //允许主线程访问数据库
                                .build();
                    }else {
                        //context null
                    }
        }
        return mRoomDatabase;
    }

    private HeartRateDao mHeartRateDao;
    public void pushDataMonitor(){
        if (mHeartRateDao == null){
            mHeartRateDao = RoomDatabaseManager.getInstance().getDatabase().heartRateDao();
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            mHeartRateDao.insert(new HeartRateData(RoomDatabaseManager.getInstance().currentAccount ,"uid" ,
                        85 , "https" , System.currentTimeMillis() , 1 , "测试" , 0));
                    mHeartRateDao.insert(new HeartRateData(RoomDatabaseManager.getInstance().currentAccount ,"uid" ,
                            95 , "https" , System.currentTimeMillis() , 1 , "测试" , 0));
        });
    }
}

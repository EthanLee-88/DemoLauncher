package com.example.demolauncher.room.HaertRate;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HeartRateDao {

    @Query("SELECT * FROM heartRate")
    List<HeartRateData> getAll();

    //根据账号条件查询
    @Query("SELECT * FROM heartRate WHERE account IN (:account)")
    List<HeartRateData> getAllByAccount(String account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(HeartRateData heartRateData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(HeartRateData... heartRateData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<HeartRateData> heartRateDataList);

    @Update
    int update(HeartRateData heartRateData);

    @Update
    int updateAll(HeartRateData... heartRateData);

    @Update
    int updateAll(List<HeartRateData> heartRateDataList);

    @Delete
    int delete(HeartRateData heartRateData);

    @Delete
    int deleteAll(HeartRateData... heartRateData);

    @Delete
    int deleteAll(List<HeartRateData> heartRateDataList);
}

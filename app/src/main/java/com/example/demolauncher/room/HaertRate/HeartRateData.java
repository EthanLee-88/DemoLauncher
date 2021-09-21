package com.example.demolauncher.room.HaertRate;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "heartRate")
public class HeartRateData {

    public HeartRateData(){}

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id")
    private int did;

    @ColumnInfo(name = "user_id")
    private String uid;

    @ColumnInfo(name = "account")
    private String account;

    @ColumnInfo(name = "heart_rate")
    private  int heartRate;

    @ColumnInfo(name = "ecg_img_url")
    private String ecgImg;

    @ColumnInfo(name = "time_millis")
    private long timeMillis;

    @ColumnInfo(name = "measure_status")
    private int status;

    @ColumnInfo(name = "remark")
    private String remark;

    @ColumnInfo(name = "data_sign")
    private int sign;

    @Ignore
    public HeartRateData(int heartRate){
        this.heartRate = heartRate;
    }
    @Ignore
    public HeartRateData(String account){
        this.account = account;
    }
    @Ignore
    public HeartRateData( String account ,String uid, int heartRate ,String ecgImg, long timeMillis , int status , String remark , int sign){
        this.uid = uid;
        this.heartRate = heartRate;
        this.ecgImg = ecgImg;
        this.timeMillis = timeMillis;
        this.status = status;
        this.remark = remark;
        this.account = account;
        this.sign = sign;
    }

    public void setDid(int did) {
        this.did = did;
    }
    public int getDid() {
        return did;
    }

    public void setAccount(String account){
        this.account = account;
    }
    public String getAccount(){
        return this.account;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }
    public int getHeartRate() {
        return heartRate;
    }

    public void setTimeMillis(long timeMillis){
        this.timeMillis = timeMillis;
    }
    public long getTimeMillis(){
        return this.timeMillis;
    }

    public void setEcgImg(String ecgImg) {
        this.ecgImg = ecgImg;
    }
    public String getEcgImg() {
        return ecgImg;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
    public int getSign() {
        return sign;
    }
}

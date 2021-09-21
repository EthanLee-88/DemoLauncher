package com.example.demolauncher.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/****************
 * 一个类实现 Parcelable接口，就需要重写两个方法：writeToParcel(Parcel out, int i)(序列化) 、describeContents（）
 *  需定义 CREATOR 变量并赋值，反序列化过程由该实例实现。
 *  此外还需一个带 Parcel形参的构造器，以实现 CREATOR 内部的反序列化对象的创建
 * ********************/
public class Hero implements Parcelable {

    private String realName;
    private String nickName;
    private String heroType;

    public Hero(){}
    public Hero(String realName , String nickName , String heroType){
        this.realName = realName;
        this.nickName = nickName;
        this.heroType =  heroType;
    }
    private Hero(Parcel in){
        readFromParcel(in);

    }

    private void readFromParcel(Parcel in){
        this.realName = in.readString();
        this.nickName = in.readString();
        this.heroType = in.readString();
    }

    public static final Creator<Hero> CREATOR  = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int i) {
            return new Hero[i];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int i) {
         out.writeString(realName);
         out.writeString(nickName);
         out.writeString(heroType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("[%s , %s , %s ]\n" , realName , nickName , heroType);
    }
}

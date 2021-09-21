package com.example.demolauncher.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeroesDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "HeroesDataBaseHelper";

    private static final String DB_NAME = "heroes_data_base";
    public static final String HEROES_TABLE_NAME = "heroes";
    private static final int DB_VERSION = 1;

    private static final String CREATE_HEROES_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + HEROES_TABLE_NAME +
            "(hero_id INTEGER PRIMARY KEY,real_name TEXT,nick_name TEXT,gender INT,job TEXT)";
// CREATE TABLE IF NOT EXISTS heroes(hero_id INTEGER PRIMARY KEY,real_name TEXT,nick_name TEXT,gender INT,job TEXT)

    public HeroesDataBaseHelper(Context context){
        super(context , DB_NAME , null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HEROES_TABLE_SQL);
        heroesInit(sqLiteDatabase);
    }

    private void heroesInit(SQLiteDatabase sqLiteDatabase){
        Cursor heroes = sqLiteDatabase.query(HEROES_TABLE_NAME , new String[]{"hero_id"} ,
                null , null , null , null , null);
        Log.d(TAG , "heroesLen=" + heroes.getCount());
        heroes.close();
        ContentValues ironMan = new ContentValues();
        ironMan.put("real_name" , "Tony");
        ironMan.put("nick_name" , "钢铁侠");
        ironMan.put("gender" , 1);
        ironMan.put("job" , "战士");
        sqLiteDatabase.insert(HEROES_TABLE_NAME , null , ironMan);
        Cursor heroesIronMan = sqLiteDatabase.query(HEROES_TABLE_NAME , new String[]{"hero_id"} ,
                null , null , null , null , null);
        Log.d(TAG , "heroesLen=" + heroesIronMan.getCount());
        heroes.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

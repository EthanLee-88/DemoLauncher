package com.example.demolauncher.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.demolauncher.provider.HeroesDataBaseHelper.HEROES_TABLE_NAME;

public class HeroesProvider extends ContentProvider {
    private static final String TAG = "HeroesProvider";
    private SQLiteDatabase mSQLiteDatabase;

    public static final String AUTHORITIES = "com.example.demolauncher.hero.provider";
    public static final Uri HEROES_TABLE_URI = Uri.parse("content://" + AUTHORITIES + "/heroes");
    public static final int HEROES_TABLE_CODE = 0;

    private static final UriMatcher uMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {uMatcher.addURI(AUTHORITIES , "heroes" , HEROES_TABLE_CODE);}

    private String getTableName(Uri uri){
        Log.d(TAG , "code=" + uMatcher.match(uri));
        String tableName = null;
        switch (uMatcher.match(uri)){
            case HEROES_TABLE_CODE : tableName = HEROES_TABLE_NAME;break;
            default:break;
        }
        return tableName;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG , "onCreate");
        mSQLiteDatabase = new HeroesDataBaseHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG , "insert");
        String tableName = getTableName(uri);
        if (tableName == null) return null;
        mSQLiteDatabase.insert(tableName , null , contentValues);
        getContext().getContentResolver().notifyChange(uri , null);
        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.d(TAG , "query");
        String tableName = getTableName(uri);
        if (tableName == null) return null;
        return mSQLiteDatabase.query(tableName , strings , s , strings1 , null , null , s1 , null);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG , "delete");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG , "update");
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG , "getType");
        return null;
    }
}

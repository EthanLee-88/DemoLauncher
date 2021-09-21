package com.example.demolauncher.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.demolauncher.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.demolauncher.provider.HeroesProvider.HEROES_TABLE_URI;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";
    private Button bindProvider , insertButton;
    private TextView heroesShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        initView();
    }

    private void initView(){
        bindProvider = findViewById(R.id.bind_provider);
        insertButton = findViewById(R.id.insert_data);
        heroesShow = findViewById(R.id.heroes_list);
        setViewListener();
    }

    private void setViewListener(){
        bindProvider.setOnClickListener((View view) -> {
            getProvider();
        });
        insertButton.setOnClickListener((View view) -> {
            insertHero();
        });
    }

    private class QueryThread extends Thread{
        @Override
        public void run() {
            getProvider();
        }
    }

    private void getProvider(){
        Uri uri = HEROES_TABLE_URI;
        Cursor heroes = getContentResolver().query(uri ,
                new String[]{"hero_id" , "real_name" , "nick_name" , "gender" , "job"} ,
                null , null , null);
        if (heroes != null){
            Log.d(TAG , "heroesLen=" + heroes.getCount());
            String heroesString = "";
            while (heroes.moveToNext()){
                Log.d(TAG , "hero=" + heroes.getString(0) + "," +
                        heroes.getString(1) + "," +
                        heroes.getString(2) + "," +
                        heroes.getInt(3) + "," +
                        heroes.getString(4));
                heroesString += heroes.getString(0) + "," +
                        heroes.getString(1) + "," +
                        heroes.getString(2) + "," +
                        (heroes.getInt(3) == 0 ? "女" : "男") + "," +
                        heroes.getString(4) + "\n";
            }
            heroesShow.setText(heroesString);
            heroes.close();
        }
    }

    private void insertHero(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("real_name" , "steve");
        contentValues.put("nick_name" , "美国队长");
        contentValues.put("gender" , 1);
        contentValues.put("job" , "战士");
        getContentResolver().insert(HEROES_TABLE_URI , contentValues);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

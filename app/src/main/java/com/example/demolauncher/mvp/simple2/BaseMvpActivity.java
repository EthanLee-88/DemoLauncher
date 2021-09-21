package com.example.demolauncher.mvp.simple2;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseMvpActivity extends AppCompatActivity {
    private static final String TAG = "BaseMvpActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView();
        Log.d(TAG, "----------onCreate");

        initView();
        initData();
    }

    abstract void initView();

    abstract void initData();

    abstract void setContentView();
}

package com.example.demolauncher.mvp.simple2;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demolauncher.R;

public class MvpActivity extends BaseMvpActivity {
    private static final String TAG = "MvpActivity";
    private TextView mvpText;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "------onCreate");
    }

    @Override
    void initView() {
        mvpText = findViewById(R.id.id_mvp);
        Log.d(TAG, "-----initView");
    }

    @Override
    void initData() {
        mvpText.setText("碧云天");
    }

    @Override
    void setContentView() {
        setContentView(R.layout.activity_mvp_simple2);
        Log.d(TAG, "-setContentView");
    }
}

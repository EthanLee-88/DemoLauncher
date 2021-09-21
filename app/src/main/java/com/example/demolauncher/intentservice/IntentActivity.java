package com.example.demolauncher.intentservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SimpleService.class);
        intent.setAction("zero");
        startService(intent);
//        intent.setAction("one");
        startService(intent);
//        intent.setAction("two");
        startService(intent);
//        intent.setAction("three");
        startService(intent);
    }

}

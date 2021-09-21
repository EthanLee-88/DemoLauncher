package com.example.demolauncher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidlibrary.Test;
import com.example.demolauncher.aidl.AidlActivity;
import com.example.demolauncher.application.BaseApplication;
import com.example.demolauncher.ecg.EcgActivity;
import com.example.demolauncher.hotfix.HotFix;
import com.example.demolauncher.messenger.MessengerActivity;
import com.example.demolauncher.provider.ProviderActivity;
import com.example.demolauncher.room.RoomDatabaseManager;
import com.example.demolauncher.socket.SocketClientActivity;

import com.example.demolauncher.util.MyViewGroup;
import com.example.demolauncher.util.SortUtil;
import com.example.demolauncher.util.TestUtil;
import com.example.demolauncher.util.ToastUtil;
import com.example.demolauncher.view.MyTextView;
import com.squareup.leakcanary.RefWatcher;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    private Button messengerButton , aidlButton , providerButton , socketButton;
    private Unbinder mUnbinder;
    Button button;

    public MainActivity(){
        System.out.println(Test.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG , "onCreate");
        setContentView(R.layout.activity_main);

        RoomDatabaseManager.getInstance().pushDataMonitor();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return null;
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return null;
            }
        });

        initView();
        x.view().inject(this);
    }

    @Event(R.id.main_layout)
    private void showToast(){
        Toast.makeText(this, "xUtil test .", Toast.LENGTH_SHORT).show();


    }

    private void initView(){

        messengerButton = findViewById(R.id.button_messenger);
        aidlButton = findViewById(R.id.button_aidl);
        providerButton = findViewById(R.id.button_provider);
        socketButton = findViewById(R.id.button_socket);
        MyViewGroup myViewGroup = findViewById(R.id.my_view);
        TextView childOne = findViewById(R.id.child_one);
        TextView childTwo = findViewById(R.id.child_two);
        setViewListener();
        MyTextView myTextView = findViewById(R.id.my_text_view);
        myTextView.setText("碧云天，黄花地");
        myTextView.setTextColor(Color.BLACK);


    }

    private void setViewListener(){
        messengerButton.setOnClickListener((View view) -> {
            startMessengerActivity();
        });
        aidlButton.setOnClickListener((View view) -> {
            startAidlActivity();
        });
        providerButton.setOnClickListener((View view) -> {
            startProviderActivity();
//            checkFix();
        });
        socketButton.setOnClickListener((View view) -> {
//            startSocketActivity();
//            checkFix();
//              test();
//            dialogShow();
//            SortUtil.sort();
//            fibonacci(15);
//            findCount(12);
//            printNumberList(5);
//            SortUtil.heapSort();
            Intent intent = new Intent(this , EcgActivity.class);
            startActivity(intent);
        });
    }

    private void test(){
//        TestUtil.showToast(this);
        new Thread(() ->{
            try {
                Thread.sleep(60 * 1000);
            }catch (InterruptedException interruptedException){}
        }).start();
    }

    private void checkFix(){
        try {
            String dexPath = Environment.getExternalStorageDirectory() + "/out.dex";
//            HotFix.fixDexFile(this, dexPath);

            Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "修复失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startMessengerActivity(){
        Intent intent = new Intent(this , MessengerActivity.class);
        startActivity(intent);
    }

    private void startAidlActivity(){
        Intent intent = new Intent(this , AidlActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void startProviderActivity(){
            Intent intent = new Intent(this , ProviderActivity.class);
            startActivity(intent);
    }

    private void startSocketActivity(){
        Intent intent = new Intent(this , SocketClientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG , "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG , "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG , "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG , "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG , "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG , "onDestroy");
        ToastUtil.showToast(this);
        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }


    private static final String TAG = "MainActivity";

    private void dialogShow(){
        Dialog dialog = new AlertDialog.Builder(this).setMessage("对话框")
                            .setTitle("对话框").create();
        dialog.show();

    }
}

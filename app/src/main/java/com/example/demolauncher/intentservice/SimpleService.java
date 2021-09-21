package com.example.demolauncher.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 外界每调用一次startService，这里的 onStartCommand就会回调一次
 * 然后通过 HandlerThread的 handler分发消息，handler 调用 onHandleIntent方法处理消息
 * handler 每次调用完 onHandleIntent会调用带参数的 stopSelf(int startId)尝试停止服务
 * 通过 startId判断是否还有任务，没有就停止。
 *
 */
public class SimpleService extends IntentService {

    public SimpleService() {
        super("SimpleService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SimpleService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SimpleService", "onStartCommand = ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("SimpleService", "onHandleIntent = ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SimpleService", "onDestroy");
    }
}

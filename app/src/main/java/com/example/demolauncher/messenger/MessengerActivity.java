package com.example.demolauncher.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demolauncher.R;
import com.example.demolauncher.application.BaseApplication;
import com.squareup.leakcanary.RefWatcher;
//import com.squareup.leakcanary.RefWatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.demolauncher.messenger.MessengerService.MSG_FROM_SERVICE;
import static com.example.demolauncher.messenger.MessengerService.STRING_FROM_SERVICE;

/***
 * 客户端可将服务端返回的 iBinder 实例作为构造器实参创建 Messenger 实例
 * 当客户端需要向服务端发消息时，需要 重新创建一个 Messenger实例（Handler 实例作为构造器参数）
 * 并创建 Message 实例且给 replyTo参数赋值，设置Bundle后使用 服务端返回的 Messenger 对象
 *  传递 Message 实现IPC通信
 * ***/
public class MessengerActivity extends AppCompatActivity {
    private static final String TAG = "MessengerActivity";
    public static final int MSG_FROM_CLIENT = 1025;
    public static final String STRING_MSG_CLIENT = "msg";
    private Messenger clientMessenger;
    private Button sentMsg , bindDevice;
    private TextView getMsg;
    private EditText inputEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG , "onStop");
        setContentView(R.layout.activity_messenger);
        initView();
        test();
    }

    private void test(){
//        TestUtil.showToast(this);
//        Log.d("Thread" , "ThreadStart" + "test");
//        new Thread(() ->{
//            try {
//                Log.d("Thread" , "ThreadStart");
//                Thread.sleep(600 * 1000);
//            }catch (InterruptedException interruptedException){}
//        }).start();
        MThread mThread = new MThread();
        mThread.start();
    }
    private class MThread extends Thread{
        @Override
        public void run() {
            super.run();
            Log.d("Thread" , "ThreadStart");
            try {
                Thread.sleep(600 * 1000);
            }catch (InterruptedException interruptedException){
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG , "onDestroy");
        unBindService();
        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
        refWatcher.watch(this);
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
        setTitle("Messenger");
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


    private void initView(){
        sentMsg = findViewById(R.id.sent_message_button);
        getMsg = findViewById(R.id.msg_show);
        bindDevice = findViewById(R.id.bind_device_button);
        inputEdit = findViewById(R.id.input_edit);
        setViewListener();
    }

    private void setViewListener(){
        sentMsg.setOnClickListener((View view) -> {
            setSentMsg();
        });
        bindDevice.setOnClickListener((View view) -> {
            clientBindService();
        });
    }

    private void clientBindService(){
        Intent serviceIntent = new Intent(this , MessengerService.class);
        bindService(serviceIntent , clientConnection , Context.BIND_AUTO_CREATE);
    }

    private void unBindService(){
        if (clientMessenger != null) unbindService(clientConnection);
    }

    private void setSentMsg(){
        if (clientMessenger != null){
            Message messageClient = Message.obtain(null , MSG_FROM_CLIENT);
            messageClient.replyTo = new Messenger(new HandlerMsgFromService());
            Bundle bundle = new Bundle();
            bundle.putString(STRING_MSG_CLIENT , inputEdit.getText().toString());
            messageClient.setData(bundle);
            try {
                clientMessenger.send(messageClient);
            }catch (Exception e){}
        }else {
            Toast.makeText(this , "请先绑定" , Toast.LENGTH_SHORT).show();
        }
    }

    private class HandlerMsgFromService extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_FROM_SERVICE : getMsg.setText(msg.getData().getString(STRING_FROM_SERVICE)); break;
                default: super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection clientConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            clientMessenger = new Messenger(iBinder);
            bindDevice.setText("绑定服务（已绑定）");
            bindDevice.setEnabled(false);
            bindDevice.setTextColor(getResources().getColor(R.color.colorGrey));
            sentMsg.setEnabled(true);
            sentMsg.setTextColor(getResources().getColor(R.color.colorBlack));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bindDevice.setText("绑定服务");
            bindDevice.setEnabled(true);
            bindDevice.setTextColor(getResources().getColor(R.color.colorBlack));
            sentMsg.setEnabled(false);
            sentMsg.setTextColor(getResources().getColor(R.color.colorGrey));
            clientMessenger = null;
        }
    };
}

package com.example.demolauncher.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demolauncher.R;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/***
 *   AIDL接口的内部类方法 HeroesInterface.Stub.asInterface(Binder binder) 用于将
 *   服务端返回的Binder对象转换成客户端所需要的 AIDL 接口类型的实例，从而实现远程调用
 * ***/
public class AidlActivity extends AppCompatActivity {
    private static final String TAG = "AidlActivity";
    private Button bindServiceButton , addHeroButton , getHeroesButton;
    private EditText realNameEdit , nickNameEdit , heroTypeEdit;
    private TextView heroesShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        initView();
    }

    private void initView(){
        bindServiceButton = findViewById(R.id.bind_service_aidl);
        addHeroButton = findViewById(R.id.add_hero);
        getHeroesButton = findViewById(R.id.get_heroes);
        realNameEdit = findViewById(R.id.input_real_name);
        nickNameEdit = findViewById(R.id.input_nick_name);
        heroTypeEdit = findViewById(R.id.input_hero_type);
        heroesShow = findViewById(R.id.heroes_show);
        setViewListener();
    }

    private void setViewListener(){
        bindServiceButton.setOnClickListener((View view) -> {
            bindAidlService();
        });
        addHeroButton.setOnClickListener((View view) -> {
            addHero();
        });
        getHeroesButton.setOnClickListener((View view) -> {
           getHeroesList();
        });
    }

    /***以设置组件名称的方式构建Intent 用以跨应用间绑定服务***/
    private void bindAidlService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.demolauncher" ,//应用的包名
                "com.example.demolauncher.aidl.AidlService"));//带路径的服务类名，用于跨应用绑定
        bindService(intent , aidlConnection , Context.BIND_AUTO_CREATE);
    }

    private void unBindService(){
        unbindService(aidlConnection);
        mHeroesInterface = null;
    }

    private HeroesInterface mHeroesInterface;
    private ServiceConnection aidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder != null){
                mHeroesInterface = HeroesInterface.Stub.asInterface(iBinder);
                bindServiceButton.setEnabled(false);
                bindServiceButton.setText(bindServiceButton.getText().toString() + "(已绑定)");
                try {
                    mHeroesInterface.registerListener(mOnNewHeroJoinListener);
                }catch (RemoteException r){}
            }else {
                Log.d(TAG , "PERMISSION_DENIED");
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mHeroesInterface = null;
            bindServiceButton.setEnabled(true);
            bindServiceButton.setText("绑定AIDL服务");
        }
    };

    private final OnNewHeroJoinListener mOnNewHeroJoinListener = new OnNewHeroJoinListener.Stub() {
        @Override
        public void onNewHeroJoin(List<Hero> heroes) throws RemoteException {
           showList(heroes);
        }
    };

    private void getHeroesList(){
        if ((mHeroesInterface != null) && (mHeroesInterface.asBinder().isBinderAlive())){
            try {
                List<Hero> heroes = mHeroesInterface.getHeroes();
                showList(heroes);
            }catch (RemoteException e){}
        }else {
            Toast.makeText(this , "请先绑定服务" , Toast.LENGTH_SHORT).show();
        }
    }

    private void showList(List<Hero> heroList){
        Log.d(TAG , "Heroes=" + heroList.toString());
        heroesShow.setText(heroList.toString());
    }

    private void addHero(){
        if (mHeroesInterface != null){
            if (realNameEdit.getText().toString().isEmpty()){
                Toast.makeText(this , "请输入姓名" , Toast.LENGTH_SHORT).show();
            }else if(nickNameEdit.getText().toString().isEmpty()){
                Toast.makeText(this , "请输入昵称" , Toast.LENGTH_SHORT).show();
            }else if (heroTypeEdit.getText().toString().isEmpty()){
                Toast.makeText(this , "请输入英雄类别" , Toast.LENGTH_SHORT).show();
            }else {
                try{
                    mHeroesInterface.addHero(new Hero(realNameEdit.getText().toString() ,
                            nickNameEdit.getText().toString() ,
                            heroTypeEdit.getText().toString()));
                }catch (RemoteException e){}
            }
        }else {
            Toast.makeText(this , "请先绑定服务" , Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG , "onDestroy");
        if (mHeroesInterface != null) unBindService();
    }
}

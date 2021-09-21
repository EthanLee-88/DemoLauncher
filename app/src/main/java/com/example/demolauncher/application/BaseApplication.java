package com.example.demolauncher.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.demolauncher.hotfix.HotFix;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class BaseApplication extends Application {
    public static Context applicationContext;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        refWatcher = setupLeakCanary();
    }

    private RefWatcher setupLeakCanary(){
        if (LeakCanary.isInAnalyzerProcess(this))
            return RefWatcher.DISABLED;
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context){
        BaseApplication baseApplication = (BaseApplication) context.getApplicationContext();
        return baseApplication.refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        checkFix();
    }

    private void checkFix(){
        try {
            String dexPath = Environment.getExternalStorageDirectory().getPath();
//            HotFix.fixDexFile(this, dexPath);

            Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "修复失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

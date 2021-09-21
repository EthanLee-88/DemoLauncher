package com.example.demolauncher.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.demolauncher.aidl.Hero;
import com.example.demolauncher.aidl.HeroesInterface;
import com.example.demolauncher.aidl.OnNewHeroJoinListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;
/*************
 * 在AIDL实现的IPC通信当中，service用于返回给客户端的binder由AIDL接口的内部类Stub
 *  实现，该类实现了IBinder接口，HeroesInterface.Stub。
 * *************/
public class AidlService extends Service {
    private static final String TAG = "AidlService";
    private CopyOnWriteArrayList<Hero> serviceHeroes = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<OnNewHeroJoinListener> Listeners = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        serviceHeroes.add(new Hero("Tony" , "钢铁侠" , "战士、坦克"));
        serviceHeroes.add(new Hero("Steve" , "美国队长" , "战士"));
        serviceHeroes.add(new Hero("Thor" , "雷神" , "坦克、战士"));
    }

    private final HeroesInterface.Stub binder = new HeroesInterface.Stub() {
        @Override
        public void addHero(Hero hero) throws RemoteException {
            serviceHeroes.add(hero);
            if (Listeners.size() > 0){
                for (OnNewHeroJoinListener listener : Listeners){
                    listener.onNewHeroJoin(serviceHeroes);
                }
            }
        }
        @Override
        public List<Hero> getHeroes() throws RemoteException {
            return serviceHeroes;
        }
        @Override
        public void registerListener(OnNewHeroJoinListener onNewHeroJoinListener) throws RemoteException {
            if (!Listeners.contains(onNewHeroJoinListener)) Listeners.add(onNewHeroJoinListener);
        }
        @Override
        public void unRegisterListener(OnNewHeroJoinListener onNewHeroJoinListener) throws RemoteException {
            if (Listeners.contains(onNewHeroJoinListener)) Listeners.remove(onNewHeroJoinListener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        int per = checkCallingOrSelfPermission("com.example.demolauncher.permission.ACCESS_HEROES_SERVICE");
//        Log.d(TAG , "check=" + per + "denied=" + PackageManager.PERMISSION_DENIED);
//        if (per == PackageManager.PERMISSION_DENIED) return null;
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

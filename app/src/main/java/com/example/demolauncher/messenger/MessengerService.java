package com.example.demolauncher.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import static com.example.demolauncher.messenger.MessengerActivity.MSG_FROM_CLIENT;
import static com.example.demolauncher.messenger.MessengerActivity.STRING_MSG_CLIENT;
/***
 *   通过给 Messenger 构造器传入 Handler 对象可创建 Messenger实例
 *   Messenger 的 getBinder 方法可获得用于返回客户端的 binder
 *   Handler 对象所返回的 Message 的replyTo 参数即为IPC通信返回的 Messenger对象
 *   可用该对象的 send(Message msg) 来向客户端发送 Message消息
 * ***/
public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    private final Messenger serviceMessenger = new Messenger(new MessengerHandle());
    public static final int MSG_FROM_SERVICE = 1026;
    public static final String STRING_FROM_SERVICE = "msg_service";

    private static class MessengerHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_FROM_CLIENT :
                    Log.d(TAG , msg.getData().getString(STRING_MSG_CLIENT));
                    setMsgToClient(msg); break;
                default:super.handleMessage(msg);
            }
        }

        private void setMsgToClient(Message clientMsg){
            Messenger messenger = clientMsg.replyTo;
            Message serviceMsg = Message.obtain(null , MSG_FROM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putString(STRING_FROM_SERVICE , "服务器已收到来自客户端的消息:" +
                    clientMsg.getData().getString(STRING_MSG_CLIENT) + ",谢谢-" + System.currentTimeMillis());
            serviceMsg.setData(bundle);
            try {
                messenger.send(serviceMsg);
            }catch (RemoteException e){}
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }
}

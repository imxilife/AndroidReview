package com.kelly.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MessengerService extends Service {

    private static final String TAG = MessengerService.class.getSimpleName();


    public static final int MSG_CLIENT_SEND = 1;
    public static final int MSG_SERVICE_SEND = 2;
    private MessengerHandler messengerHandler;
    private Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"MessengerService onCreate...");
        messengerHandler = new MessengerHandler(this);
        messenger = new Messenger(messengerHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"MessengerServie onBind ...");
        return messenger.getBinder();
    }


    static final class MessengerHandler extends Handler{

        WeakReference<MessengerService>mRef;

        MessengerHandler(MessengerService service) {
            this.mRef = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {

            MessengerService service = mRef.get();
            if(service==null){
                return;
            }

            switch (msg.what){
                case MSG_CLIENT_SEND:
                    Bundle bundle = msg.getData();
                    Log.i(TAG,"接收到服务端发送的消息是: "+bundle.getString("key"));
                    Messenger messenger = msg.replyTo;
                    Message message = Message.obtain(null,MSG_SERVICE_SEND);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("key","服务端已经收到消息");
                    message.setData(bundle1);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}

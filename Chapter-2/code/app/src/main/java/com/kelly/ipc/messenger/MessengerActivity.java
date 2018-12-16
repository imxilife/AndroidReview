package com.kelly.ipc.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = MessengerActivity.class.getSimpleName();
    private Messenger messenger;

    private Messenger mClientMessenger;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientMessenger = new Messenger(new ClientMessengerHandler());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {  //绑定成功后的回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG,"onServiceConnected");
            messenger = new Messenger(binder);
            Message message = Message.obtain(null,MessengerService.MSG_CLIENT_SEND);
            Bundle bundle = new Bundle();
            bundle.putString("key","hello this is client");
            message.setData(bundle);
            message.replyTo = mClientMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"onServiceDisconnected");
        }
    };

    private static final class ClientMessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.MSG_SERVICE_SEND:
                    Bundle bundle = msg.getData();
                    Log.i(TAG,"服务端返回的:" + bundle.getString("key"));
                    break;
            }
        }
    }

}

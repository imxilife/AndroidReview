package com.kelly.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class IntentService extends Service {


    private final String TAG = IntentService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service creating ...");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            Bundle bundle = intent.getBundleExtra("key");
            Book book =bundle.getParcelable("book_info");
            Log.i(TAG,"BOOK:"+(book!=null?book.toString():"this is empty info"));
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

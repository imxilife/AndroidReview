package com.kelly.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyService extends Service {

    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Ex(),5000);
    }

    final class Ex implements  Runnable{
        @Override
        public void run() {
           // Toast.makeText(MyService.this,"服务中打印的sUID值是: "+UserManager.sUID++,Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this, "多进程下的单例模式:"+TestManager.getManager(), Toast.LENGTH_SHORT).show();
            //mHandler.postDelayed(new Ex(),2000);
            Intent intent = new Intent(MyService.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

}

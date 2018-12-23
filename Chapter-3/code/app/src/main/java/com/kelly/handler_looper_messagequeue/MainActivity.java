package com.kelly.handler_looper_messagequeue;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyHandler myHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"main Thread ID:"+Process.myTid());
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyThread handlerThread = new MyThread();
        handlerThread.start();
        myHandler = new MyHandler(this,handlerThread.getLooper());
        Message msg = myHandler.obtainMessage(MyHandler.MSG_TEST);
        msg.sendToTarget();
    }

    final static class MyThread extends Thread{

        private Looper mLooper;

        @Override
        public void run() {

            Log.i(TAG,"current Thread ID:"+Process.myTid());

            Looper.prepare();

            synchronized (this){
                mLooper = Looper.myLooper();
                notifyAll();
            }

            Looper.loop();

        }

        public Looper getLooper(){
            if(!isAlive()){
                return null;
            }

            synchronized (this){
                if(isAlive() && mLooper==null){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return mLooper;
        }

    }

    private static final class MyHandler extends Handler{

        private static final int MSG_TEST = 1;
        private WeakReference<MainActivity>activity;

        MyHandler(MainActivity mainActivity,Looper looper){
            super(looper);
            this.activity = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activity==null){
                return ;
            }

            switch (msg.what){

                case MSG_TEST:
                    Log.i(TAG,"process TID:"+ Process.myTid());
                    break;


            }

        }
    }

}

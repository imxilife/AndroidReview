package com.kelly.multi_thread.sleep;

import android.util.Log;

import com.kelly.multi_thread.Constant;

import java.io.ObjectInputStream;

public class ThreadB extends Thread {


    private Object object;


    public ThreadB(Object object) {
        this.object = object;
    }

    @Override
    public void run() {

        Log.i(Constant.TAG,"此时线程B的状态是:"+Thread.currentThread().getState());
        synchronized (object){
            Log.i(Constant.TAG,"线程B 进入临界区执行");
        }

        Log.i(Constant.TAG,"B 线程执行完毕退出");

    }
}

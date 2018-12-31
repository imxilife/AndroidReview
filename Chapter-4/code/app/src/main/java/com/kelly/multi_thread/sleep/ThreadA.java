package com.kelly.multi_thread.sleep;

import android.util.Log;

import com.kelly.multi_thread.Constant;

import java.io.ObjectInputStream;

public class ThreadA extends Thread {


    private Object object;

    public ThreadA(Object object) {
        this.object = object;
    }

    @Override
    public void run() {


        synchronized (object){
            try {
                Log.i(Constant.TAG,"临界区 线程A开始睡眠10分钟");
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(Constant.TAG,"A 线程被中断");
            }
        }
        Log.i(Constant.TAG,"A 线程执行完毕退出");
    }
}

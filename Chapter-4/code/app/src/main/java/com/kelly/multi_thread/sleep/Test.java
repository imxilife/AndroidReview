package com.kelly.multi_thread.sleep;

import android.util.Log;

import com.kelly.multi_thread.Constant;

public class Test {


    private final Object object = new Object();


    synchronized public void synMethod(){

        Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入同步方法");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出同步方法");
    }

    public void method(){

        Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入普通方法");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出普通方法");

    }

    synchronized public static void staticSyncMethond(){

        Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入静态同步方法");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出静态同步方法");

    }

    public static void staticMethod(){

        Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入静态方法");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出静态方法");

    }

    public void syncObj(){

        synchronized (object){
            Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入 object 同步代码块");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出 object 同步代码块");
        }
    }

    public void syncThis(){

        synchronized (this){
            Log.i(Constant.TAG,Thread.currentThread().getName()+" 进入 this 同步代码块");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(Constant.TAG,Thread.currentThread().getName()+" 退出 this 同步代码块");
        }
    }

}

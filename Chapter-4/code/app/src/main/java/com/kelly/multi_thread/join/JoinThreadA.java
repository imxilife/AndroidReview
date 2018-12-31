package com.kelly.multi_thread.join;

import android.util.Log;

import com.kelly.multi_thread.Constant;

public class JoinThreadA extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 500000; i++) {
            if(i%100000==0){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(Constant.TAG,"A 线程当前:"+i);
            }
        }
        Log.e(Constant.TAG,"线程A 执行完毕...");
    }
}

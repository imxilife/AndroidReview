package com.kelly.multi_thread.join;

import android.util.Log;

import com.kelly.multi_thread.Constant;

public class JoinThreadB extends Thread {
    

    private JoinThreadA joinThreadA;

    public JoinThreadB(JoinThreadA joinThreadA) {
        this.joinThreadA = joinThreadA;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50000; i++) {
             if(i%10000==0){
                 Log.d(Constant.TAG,"B 线程当前:"+i);
             }
        }
        try {
            joinThreadA.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(Constant.TAG,"B 线程 退出JOIN状态");
        }
        Log.d(Constant.TAG,"线程B 执行完毕...");
    }
}

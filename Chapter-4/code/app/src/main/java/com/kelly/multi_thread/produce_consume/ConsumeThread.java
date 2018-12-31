package com.kelly.multi_thread.produce_consume;

import android.util.Log;

import com.kelly.multi_thread.Constant;

import java.util.Queue;

public class ConsumeThread extends Thread {


    private Queue<Integer>queue;

    public ConsumeThread(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        while(true){
            synchronized (queue){
                while (queue.isEmpty()){
                    try {
                        Log.i(Constant.TAG,"队列容量为0，暂时没有可消耗的资源，消费者线程进入等待状态");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i(Constant.TAG,"消费者 wait 中断被打断");
                    }
                }
                int value = queue.remove();
                Log.i(Constant.TAG,"消费者线程本次取到的value:"+value);
                queue.notifyAll();
            }
        }

    }
}

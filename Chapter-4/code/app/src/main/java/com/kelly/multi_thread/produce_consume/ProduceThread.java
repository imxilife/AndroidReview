package com.kelly.multi_thread.produce_consume;

import android.util.Log;

import com.kelly.multi_thread.Constant;

import java.util.Queue;
import java.util.Random;

public class ProduceThread extends Thread {

    private Queue<Integer>queue;

    public ProduceThread(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            synchronized (queue){
                while (queue.size() == 10){
                    try {
                        Log.i(Constant.TAG,"队里已到达最大容量，无法继续装载，生产者线程进入等待状态");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Random random = new Random();
                int value = random.nextInt();
                Log.i(Constant.TAG, "生产者线程产生了value:"+value);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(value);
                queue.notifyAll();
            }
           /* try {
                Thread.sleep(3000);  //模拟生产一个数据的耗时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}

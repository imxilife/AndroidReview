package com.kelly.multi_thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kelly.multi_thread.join.JoinThreadA;
import com.kelly.multi_thread.join.JoinThreadB;
import com.kelly.multi_thread.produce_consume.ConsumeThread;
import com.kelly.multi_thread.produce_consume.ProduceThread;
import com.kelly.multi_thread.sleep.ThreadA;
import com.kelly.multi_thread.sleep.ThreadB;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Queue<Integer>mQueue;

    private static Object lock = new Object();

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    private ProduceThread mProduceThread;
    private ConsumeThread mConsumeThread;
    private ThreadA mThreadA;
    private ThreadB mThreadB;

    private JoinThreadA mJoinThreadA;
    private JoinThreadB mJoinThreadB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn1 = (Button) findViewById(R.id.irp_for_sleep);
        mBtn2 = (Button) findViewById(R.id.irp_for_wait);
        mBtn3 = (Button) findViewById(R.id.irp_for_syncnoridzed);
        mQueue = new LinkedList<>();
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


        initPart_1();

        initPart_2();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.irp_for_sleep:
                click_1();
                break;
            case R.id.irp_for_wait:
                click_2();
                break;
            case R.id.irp_for_syncnoridzed:
                click_3();
                break;

            default:
                break;
        }
    }

    private void click_1(){
        mConsumeThread.interrupt();
    }

    private void click_2(){
      //mThreadA.interrupt();  //sleep被中断
        mThreadB.interrupt();  //BLOCKED被中断
    }

    private void click_3(){
      mJoinThreadB.interrupt();
    }

    private void initPart_1(){
        // ProduceThread produceThread = new ProduceThread(mQueue);
        // ConsumeThread consumeThread = new ConsumeThread(mQueue);
        // produceThread.start();
        // consumeThread.start();


        //  ThreadA threadA = new ThreadA(lock);
        //  ThreadB threadB = new ThreadB(lock);

        //在临界区内部调用sleep方法线程不会释放锁
        // threadA.start();
        // threadB.start();


        //   JoinThreadA joinThreadA = new JoinThreadA();
        //   JoinThreadB joinThreadB = new JoinThreadB(joinThreadA); //join会让线程进入wait状态。等待另一个线程执行完run方法。自己才继续往下走。
        //   joinThreadA.start();
        //   joinThreadB.start();

    }

    private void initPart_2(){

        //wait
        mProduceThread = new ProduceThread(mQueue);
        mConsumeThread = new ConsumeThread(mQueue);
       // mProduceThread.start();
       // mConsumeThread.start();

        //sleep
        mThreadA = new ThreadA(lock);
        mThreadB = new ThreadB(lock);
        mThreadA.start();
        mThreadB.start();

        //join
        mJoinThreadA = new JoinThreadA();
        mJoinThreadB = new JoinThreadB(mJoinThreadA);
       // mJoinThreadA.start();
       // mJoinThreadB.start();

    }

}

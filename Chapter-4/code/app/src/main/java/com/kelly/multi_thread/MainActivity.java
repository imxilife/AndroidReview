package com.kelly.multi_thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kelly.multi_thread.join.JoinThreadA;
import com.kelly.multi_thread.join.JoinThreadB;
import com.kelly.multi_thread.produce_consume.ConsumeThread;
import com.kelly.multi_thread.produce_consume.ProduceThread;
import com.kelly.multi_thread.sleep.Test;
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

        initPart_3();


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
       // mThreadA = new ThreadA(lock);
       // mThreadB = new ThreadB(lock);
       // mThreadA.start();
        //mThreadB.start();

        //join
        mJoinThreadA = new JoinThreadA();
        mJoinThreadB = new JoinThreadB(mJoinThreadA);
       // mJoinThreadA.start();
       // mJoinThreadB.start();

    }

    /**
     * synchronized不同的加锁方式所带来的影响
     */
    private void initPart_3(){

        Test test = new Test();

        //测试 线程A执行非静态同步方法，线程B执行非同步方法
        /**
         * 结果:
         * 线程A 进入同步方法
         * 线程B 进入普通方法
         * 线程B 退出普通方法
         * 线程A 退出同步方法
         *
         * 结论: 不同线程分别执行同个对象的同步方法和非同步方法两者是相互不影响的，同步方法的执行不会阻塞非同步方法，可以看出异步执行。
         */
      /*  ThreadA a = new ThreadA(test);
        ThreadB b = new ThreadB(test);
        a.start();
        b.start();*/

        //</>
        //测试 线程A执行非静态同步方法，线程B执行非同步方法
        /**
         * 结果:
         * 线程A 进入同步方法
         * 线程A 退出同步方法
         * 线程B 进入同步方法
         * 线程B 退出同步方法
         *
         * 结论: 不同线程执行同个对象的同个同步方法，是互斥的，同一时刻只能允许一个线程执行该方法。
         */

        //测试 线程A执行A对象的非静态同步方法，线程B执行B对象的非静态同步方法
       /* Test test1 = new Test();
        Test test2 = new Test();
        ThreadA aa = new ThreadA(test1);
        ThreadB bb = new ThreadB(test2);
        aa.start();
        bb.start();*/

        /**
         * 结果:
         * 线程A 进入同步方法
         * 线程B 进入同步方法
         * 线程A 退出同步方法
         * 线程B 退出同步方法
         *
         * 结论: 两个线程分别执行两个不同实例的同步方法，两者之间同时执行同步方法，相互不影响。
         * 原因: synchronized 在普通方法加锁 锁住的是当前类的实例 。所以在不同线程间执行类的不同对象的同一同步方法时是相互不影响的
         */


        // /测试 线程A执行A对象的静态同步方法，线程B执行B对象的静态方法
        /**
         *
         * 结果:
         * 线程A 进入静态同步方法
         * 线程B 进入静态方法
         * 线程B 退出静态方法
         * 线程A 退出静态同步方法
         *
         * 结论: 执行类的静态方法和非静态方法两者相互之间是不影响的，同样的一个线程执行静态同步方法不会阻塞另一个线程执行静态方法
         *
         */
        // /测试 线程A、B 执行静态同步方法
        /**
         *
         * 结果:
         * 线程B 进入静态同步方法
         * 线程B 退出静态同步方法
         * 线程A 进入静态同步方法
         * 线程A 退出静态同步方法
         *
         * 结论: 不同线程执行同一静态方法是互斥的，在同一时刻只能有一个线程执行这个方法
         *
         */

        // /测试 线程A、B A执行静态同步方法 B执行同步方法
        /**
         * 线程A 进入静态同步方法
         * 线程B 进入同步方法
         * 线程A 退出静态同步方法
         * 线程B 退出同步方法
         *
         * 结论: 静态同步方法的锁是当前类对象(类本身也是一种对象)，同步方法的锁是当前类的对象。由于不是同一把锁，所以相互之前不影响
         */

        //测试 线程A、B 访问object作为锁的同步代码块
        /**
         * 结果:
         * 线程A 进入 object 同步代码块
         * 线程A 退出 object 同步代码块
         * 线程B 进入 object 同步代码块
         * 线程B 退出 object 同步代码块
         */
        Test test1 = new Test();
        ThreadA threadA = new ThreadA(test1);
        ThreadB threadB = new ThreadB(test1);
        threadA.start();
        threadB.start();

        //测试 线程A、B 访问object作为锁的同步代码块
        /**
         * 结果：
         * 线程A 进入 object 同步代码块
         * 线程B 进入 this 同步代码块
         * 线程A 退出 object 同步代码块
         * 线程B 退出 this 同步代码块
         *
         */

        //测试 线程A访问this作为锁的同步代码块 线程B访问同步方法
        /**
         * 结果:
         * 线程A 进入 this 同步代码块
         * 线程A 退出 this 同步代码块
         * 线程B 进入同步方法
         * 线程B 退出同步方法
         */

    }

}

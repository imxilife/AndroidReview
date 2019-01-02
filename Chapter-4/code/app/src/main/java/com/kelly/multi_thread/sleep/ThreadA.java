package com.kelly.multi_thread.sleep;

public class ThreadA extends Thread {


    private Object object;
    private Test mTest;

  /*  public ThreadA(Object object) {
        this.object = object;
    }*/

    public ThreadA(Test test) {
        this.mTest = test;
    }

    @Override
    public void run() {

        setName("线程A");

      /*  synchronized (object){
            try {
                Log.i(Constant.TAG,"临界区 线程A开始睡眠10分钟");
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(Constant.TAG,"A 线程被中断");
            }
        }*/
        //Log.i(Constant.TAG,"A 线程执行完毕退出");

        //mTest.synMethod(); //执行同步方法
        //mTest.synMethod(); //执行同步方法
        //Test.staticSyncMethond(); //执行静态同步方法
        //mTest.syncObj();
        mTest.syncThis();
    }
}

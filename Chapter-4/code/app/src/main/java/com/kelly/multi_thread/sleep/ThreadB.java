package com.kelly.multi_thread.sleep;

public class ThreadB extends Thread {


    private Object object;
    private Test mTest;

    /*public ThreadB(Object object) {
        this.object = object;
    }*/


    public ThreadB(Test test) {
        this.mTest = test;
    }

    @Override
    public void run() {

        setName("线程B");
      /*  Log.i(Constant.TAG,"此时线程B的状态是:"+Thread.currentThread().getState());
        synchronized (object){
            Log.i(Constant.TAG,"线程B 进入临界区执行");
        }

        Log.i(Constant.TAG,"B 线程执行完毕退出");*/
       // mTest.method();  //执行普通方法
        mTest.synMethod(); //执行同步方法
       // Test.staticMethod(); //执行静态方法
        //Test.staticSyncMethond();
        //mTest.synMethod();
        //mTest.syncObj();
        //mTest.syncThis();
    }
}

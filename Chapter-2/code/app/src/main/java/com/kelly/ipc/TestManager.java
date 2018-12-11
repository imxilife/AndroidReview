package com.kelly.ipc;

public class TestManager {

    private static TestManager sTestManager;
    private TestManager(){}

    public static TestManager getManager(){
        synchronized (TestManager.class){
            if(sTestManager==null){
                sTestManager = new TestManager();
            }
        }
        return sTestManager;
    }
}

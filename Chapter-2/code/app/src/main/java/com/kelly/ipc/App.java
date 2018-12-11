package com.kelly.ipc;

import android.app.Application;
import android.util.Log;

public class App extends Application {

    private final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"App onCreate called");
    }
}

package com.kelly.ipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class IntentReceiver extends BroadcastReceiver {
    private static final String TAG = IntentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("key");
        if(bundle!=null){
            Log.i(TAG,(bundle.getParcelable("book")).toString());
        }
    }
}
package com.kelly.ipc.bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class IntentActivity extends AppCompatActivity {

    private static final String TAG = IntentActivity.class.getSimpleName();
    private Bundle value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent!=null && intent.getBundleExtra("key_bundle")!=null){
            value = intent.getBundleExtra("key_bundle");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,value.getString("key"),Toast.LENGTH_SHORT).show();
    }
}

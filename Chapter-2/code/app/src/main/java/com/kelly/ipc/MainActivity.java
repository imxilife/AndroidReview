package com.kelly.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mBtn;
    private Button mBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn1 = (Button) findViewById(R.id.btn1);
        Log.i(TAG,"MainActivity.this:"+MainActivity.this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"当前sUID的值: "+ (UserManager.sUID++),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.kelly.ipc","com.kelly.ipc.MyService"));
                startService(intent);
            }
        });

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"单例值:"+TestManager.getManager(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.kelly.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Messenger;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kelly.ipc.aidl.AIDLActivity;
import com.kelly.ipc.messenger.MessengerActivity;
import com.kelly.ipc.socket.Client;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mBtn;
    private Button mBtn1;

    private  Button mBtn2;
    private  Button mBtn3;
    private  Button mBtn4;
    private  Button mBtn5;
    private  Button mBtn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button)findViewById(R.id.btn);
        mBtn1 = (Button)findViewById(R.id.btn1);
        mBtn2 = (Button)findViewById(R.id.btn2);
        mBtn3 = (Button)findViewById(R.id.btn3);
        mBtn4 = (Button)findViewById(R.id.btn4);
        mBtn5 = (Button)findViewById(R.id.btn5);
        mBtn6 = (Button)findViewById(R.id.btn6);
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

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"使用Bundle或者Intent实现Activity/Service/Receiver 进程间通信");
                //Android四大组件之三(Activity,Service、BroadCastReceiver)，可以在一个进程中通过Intent方式来启动另一个进程的Activity、Service，或者广播交互.

                Bundle bundle = new Bundle();

                //跨进程启动Activity
               /* Intent startActivityIntent = new Intent("com.kelly.action.intentActivity"); //隐式启动
                bundle.putString("key","hello i come from MainActivity");
                startActivityIntent.putExtra("key_bundle",bundle);
                startActivity(startActivityIntent);*/

                //跨进程启动Service
                //5.0以后不支持隐式启动 显式启动的两种方式 支持Component或者构建action然后设置PackageName
                Bundle bundle1 = new Bundle();
                Intent startServiceIntent = new Intent();
                Book book = new Book();
                book.setAuthor("钱钟书");
                book.setBookName("围城");
                bundle1.putParcelable("book_info",book);
                //方式一
                startServiceIntent.setAction("com.keylly.action.start_service");
                startServiceIntent.setPackage("com.kelly.ipc");
                //方式二
                //startServiceIntent.setComponent(new ComponentName("com.kelly.ipc","com.kelly.ipc.IntentService"));
                startServiceIntent.putExtra("key",bundle1);
                startService(startServiceIntent);

                //跨进程发送广播
                Bundle bundle2 = new Bundle();
                Intent startReceiverIntent = new Intent("com.kelly.action.receiver");
                bundle2.putParcelable("book",book);
                startReceiverIntent.putExtra("key",bundle2);
                sendBroadcast(startReceiverIntent);
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"使用Messenger实现进程间通信");
                Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
                startActivity(intent);
            }
        });

        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"使用AIDL实现进程间通信");
                Intent intent = new Intent(MainActivity.this, AIDLActivity.class);
                startActivity(intent);
            }
        });

        mBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"使用Socket实现进程间通信");
                Intent intent = new Intent(MainActivity.this, Client.class);
                startActivity(intent);
            }
        });

        mBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"使用ContentProvider实现进程间通信");
            }
        });
    }
}

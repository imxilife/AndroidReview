package com.kelly.ipc.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kelly.ipc.App;
import com.kelly.ipc.MainActivity;
import com.kelly.ipc.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketAddress;

public class Client extends AppCompatActivity {

    private static final String TAG = Client.class.getSimpleName();
    private Button mSend;
    private TextView mReceiver;
    private EditText mEdit;
    private Socket mSocket;
    private static final int MSG_SEND_MESSAGE = 1;
    private static final int MSG_RECEIVER_MESSAGE = 2;
    private static final int MSG_CONNECT_SERVICE = 3;
    private SocketHandler mSocketHandler;
    private BufferedWriter mBufferedWrite;
    private BufferedReader mBufferedReader;
    public static Boolean isExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_layout);
        mSend = (Button) findViewById(R.id.send);
        mReceiver = (TextView) findViewById(R.id.receive);
        mEdit = (EditText) findViewById(R.id.edit);
        HandlerThread handlerThread = new HandlerThread("socket");
        handlerThread.start();
        mSocketHandler = new SocketHandler(this,handlerThread.getLooper());
        Intent intent = new Intent(this,SocketService.class);
        startService(intent);
        mSocketHandler.sendEmptyMessageDelayed(MSG_CONNECT_SERVICE,5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEdit.getText().toString();
               if(TextUtils.isEmpty(content)){
                   Toast.makeText(Client.this,"不能发送空消息",Toast.LENGTH_SHORT).show();
               }else{
                   Message msg = mSocketHandler.obtainMessage(MSG_SEND_MESSAGE);
                   msg.obj = content;
                   mSocketHandler.sendMessage(msg);
               }
            }
        });
    }




    private Socket connectService(){
        try {
            return new Socket("192.168.1.102",9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void Smg(String message){
        if(mSocket!=null && mSocket.isConnected()){
            if(mBufferedWrite!=null){
                try {
                    mBufferedWrite.write(message+"\n");
                    mBufferedWrite.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG,message +" 发送完毕...");
            }
        }else{
            Log.i(TAG,"已失去和服务端的连接...");
        }

    }

    private void handleReceiverMessage() {

        if(mSocket!=null && mSocket.isConnected()){
            if (mBufferedReader!=null){
                while(!isExit){
                    String content;
                    final StringBuilder builder = new StringBuilder();
                    try{
                        while((content=mBufferedReader.readLine())!=null){
                                builder.append(content);
                        }
                        if(builder.length()>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mReceiver.setText(builder.toString());
                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     *
     * 总结:
     * 1、如果接收方是通过readLine()方法来读数据的话 发送方在发送的末尾一定要加'\n'换行符 否则会导致接收方一直读不到换行符而获取不到发送的数据
     * 2、发送方在数据发送完毕时要调用flush()方法将缓存区的数据写入流
     * 3、不要在Handler中收、发数据 会导致Handler阻塞
     * 4、一次通信完就关闭Socket输入流会导致Socket本身被关闭。因此如果想一直用这个Socket的话，需要在建立连接的时候把Socket输入、输出流对象保存为全局，只有不需要的时候才释放掉
     */


    private static final class SocketHandler extends Handler{

        WeakReference<Client> mRef;

        SocketHandler(Client activity,Looper looper) {
            super(looper);
            this.mRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Client activity = mRef.get();
            if(activity==null){
                Log.i(TAG,"activity is null return");
                return;
            }
            switch (msg.what){
                case MSG_SEND_MESSAGE:
                    String content = (String) msg.obj;
                    if(!TextUtils.isEmpty(content)){
                        activity.Smg(content);
                    }
                    break;

                case MSG_RECEIVER_MESSAGE:
                    activity.handleReceiverMessage();
                    break;

                case MSG_CONNECT_SERVICE:
                    activity.mSocket = activity.connectService();
                    Log.i(TAG,"socket:"+activity.mSocket);
                    try {
                        activity.mBufferedWrite = new BufferedWriter(new OutputStreamWriter(activity.mSocket.getOutputStream()));
                        activity.mBufferedReader = new BufferedReader(new InputStreamReader(activity.mSocket.getInputStream()));
                        //sendEmptyMessage(MSG_RECEIVER_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isExit = true;
        if(mBufferedReader!=null){
            try {
                mBufferedReader.close();
                mBufferedReader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mBufferedWrite!=null){
            try {
                mBufferedWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBufferedWrite = null;
        }
        if(mSocket!=null && mSocket.isConnected()){
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

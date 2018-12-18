package com.kelly.ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.icu.util.Output;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService extends Service {

    private boolean isDestroyService = false;
    private String TAG = SocketService.class.getSimpleName();

    private BufferedReader mBufferRead;
    private BufferedWriter mPrintWriter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                createServiceSocket();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyService = true;
    }

    /**
     *
     * 1、建立服务端Socket，在某个固定端口监听消息
     * 2、监听到客户端连接建立session，然后开线程单独处理
     * 3、完成通信，断开连接
     *
     */

    private void createServiceSocket(){
        try {
            ServerSocket serverSocket = new ServerSocket(9090);  //建立服务端Socket
            Log.i(TAG,"创建服务端链接...");
            while(!isDestroyService){
                final Socket socket = serverSocket.accept(); //监听连接到整个socket的客户端 (这个方法是阻塞直到有连接过来)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(socket!=null){
                            try {
                                Log.i(TAG,"连接来自 "+socket.getInetAddress().getHostAddress() +" 客户端");
                                mBufferRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                Log.i(TAG,"mBufferRead:"+mBufferRead);
                                //mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                                mPrintWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                Log.i(TAG,"mPrintWriter:"+mPrintWriter);
                                String msg = mBufferRead.readLine();
                                //Log.i(TAG,"接收消息:"+msg);
                                while(!("bye".equals(msg))){
                                  //  mPrintWriter.println("我已收到信息【"+ msg +"】来自服务端");
                                    Log.i(TAG,"接收消息:"+msg);
                                    mPrintWriter.write("服务端接收到消息: "+msg);
                                    mPrintWriter.flush();
                                    msg = mBufferRead.readLine();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }finally {
                                try {
                                    mBufferRead.close();
                                    mPrintWriter.close();
                                    socket.close();
                                    Log.i(TAG,"关闭Socket");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Log.i(TAG,"客户端已经断开连接...");
                        }

                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

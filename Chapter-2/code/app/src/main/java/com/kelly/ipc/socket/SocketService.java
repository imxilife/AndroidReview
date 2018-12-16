package com.kelly.ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService extends Service {
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            Socket socket = serverSocket.accept(); //监听连接到整个socket的客户端 (这个方法是阻塞直到有连接过来)
            socket.setKeepAlive(true);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(){

    }

}

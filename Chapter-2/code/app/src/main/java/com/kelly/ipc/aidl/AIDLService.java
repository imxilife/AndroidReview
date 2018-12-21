package com.kelly.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kelly.ipc.Book;
import com.kelly.ipc.IBookManager;
import com.kelly.ipc.IOnNewBookArrivedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AIDLService extends Service {

    private String TAG = AIDLService.class.getSimpleName();

    //private List<Book> mBookList = new ArrayList<>(10);
    private CopyOnWriteArrayList<Book>mBookList = new CopyOnWriteArrayList<>();  //线程安全集合
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();
    private AddBookRunnable mAddBookRunnable;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book("Android艺术探索","任玉刚");
        Book book1 = new Book("App研发录","包建强");
        Book book2 = new Book("Github入门与实践","大潨弘记");
        mBookList.add(book);
        mBookList.add(book1);
        mBookList.add(book2);
        mAddBookRunnable = new AddBookRunnable();
        mHandler = new Handler();
        mHandler.postDelayed(mAddBookRunnable,5000);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getListBook() throws RemoteException {
            Log.i(TAG,"book.size:"+mBookList.size());
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            Log.i(TAG,"addBook: "+book.toString());
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
            Log.i(TAG,"注册后监听器的个数:"+listeners.getRegisteredCallbackCount());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
           listeners.unregister(listener);
            Log.i(TAG,"解除注册后监听器的个数:"+listeners.getRegisteredCallbackCount());
        }
    };

    private class AddBookRunnable implements Runnable{

        @Override
        public void run() {
            Book book = new Book();
            book.setBookName("Android开发第 " + System.currentTimeMillis() +" 集");
/*            for (int i = 0; i < listeners.; i++) {
                try {
                    listeners.get(i).onNewBookArrived(book);
                    mHandler.postDelayed(mAddBookRunnable,5000);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }*/
            final int n = listeners.beginBroadcast();
            for (int i = 0; i < n; i++) {
                IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
                try {
                    listener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            listeners.finishBroadcast();
        }
    }

}

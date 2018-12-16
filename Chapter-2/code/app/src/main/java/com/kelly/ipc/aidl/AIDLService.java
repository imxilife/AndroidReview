package com.kelly.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kelly.ipc.Book;
import com.kelly.ipc.IBookManager;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {

    private String TAG = AIDLService.class.getSimpleName();

    private List<Book> mBookList = new ArrayList<>(10);

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book("Android艺术探索","任玉刚");
        Book book1 = new Book("App研发录","包建强");
        Book book2 = new Book("Github入门与实践","大潨弘记");
        mBookList.add(book);
        mBookList.add(book1);
        mBookList.add(book2);
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
    };
}

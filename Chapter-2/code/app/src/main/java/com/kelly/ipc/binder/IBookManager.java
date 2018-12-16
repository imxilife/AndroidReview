package com.kelly.ipc.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.kelly.ipc.Book;

import java.util.List;

public interface IBookManager extends IInterface {

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;


    static final String DESCRIPTOR = "com.kelly.ipc";

    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;

    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;


}

// IBookManager.aidl
package com.kelly.ipc;

// Declare any non-default types here with import statements


import com.kelly.ipc.Book;
import com.kelly.ipc.IOnNewBookArrivedListener;

interface IBookManager {

   List<Book> getListBook();

   void addBook(in Book book);

   void registerListener(IOnNewBookArrivedListener listener);

   void unRegisterListener(IOnNewBookArrivedListener listener);

}

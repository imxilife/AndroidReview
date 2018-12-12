// IBookManager.aidl
package com.kelly.ipc;

// Declare any non-default types here with import statements


import com.kelly.ipc.book;

interface IBookManager {

   List<Book> getListBook();

   void addBook(in Book book);

}

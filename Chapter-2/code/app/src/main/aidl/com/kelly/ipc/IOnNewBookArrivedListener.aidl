// IOnNewBookArrivedListener.aidl
package com.kelly.ipc;

// Declare any non-default types here with import statements
import com.kelly.ipc.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}

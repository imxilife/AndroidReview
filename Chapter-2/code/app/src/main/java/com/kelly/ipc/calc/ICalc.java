package com.kelly.ipc.calc;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface ICalc extends IInterface{


     String DESCRIPTOR = "com.kelly.ipc";

     int TRANSACTION_add = IBinder.FIRST_CALL_TRANSACTION + 0;

     int TRANSACTION_sub = IBinder.FIRST_CALL_TRANSACTION + 1;

     void add(Param param) throws RemoteException;

     void sub(Param param) throws RemoteException;
}

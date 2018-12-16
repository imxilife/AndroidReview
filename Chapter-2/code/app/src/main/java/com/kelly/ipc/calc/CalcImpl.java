package com.kelly.ipc.calc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CalcImpl extends Binder implements ICalc {


    public CalcImpl() {
        attachInterface(this,DESCRIPTOR);
    }

    public static ICalc asInterface(IBinder obj){
        if(obj == null){
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if(iin!=null && iin instanceof ICalc){  //如果服务端和客户端在一个进程内返ICalc的真正实现者
                                                //如果服务端和客户端在不同进程返回ICalc内部代理对象 (这样做是为了不给客户端暴露服务端的真实实现，只能通过代理来访问)
            return (ICalc) iin;
        }
        return new CalcImpl.Proxy(obj);
    }

    @Override
    public void add(Param param) {
        //未实现
    }

    @Override
    public void sub(Param param) {
        //未实现
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {

        switch (code){

            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                reply.writeNoException();
                return true;

            case TRANSACTION_add:
                data.enforceInterface(DESCRIPTOR);
                Param add = Param.CREATOR.createFromParcel(data);
                this.add(add);
                reply.writeNoException();
                return true;

            case TRANSACTION_sub:
                data.enforceInterface(DESCRIPTOR);
                Param sub =Param.CREATOR.createFromParcel(data);
                this.sub(sub);
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static final class Proxy implements ICalc{

        private IBinder mRemote;
        public Proxy(IBinder remote) {
            this.mRemote = remote;
        }

        @Override
        public void add(Param param) throws RemoteException {

            Parcel data  = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                param.writeToParcel(data,0);
                mRemote.transact(TRANSACTION_add,data,reply,0);
                reply.readException();
            }finally {
                data.recycle();
                reply.recycle();
            }

        }

        @Override
        public void sub(Param param) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                param.writeToParcel(data,0);
                mRemote.transact(TRANSACTION_sub,data,reply,0);
                reply.readException();
            }finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}

package com.kelly.ipc.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kelly.ipc.Book;

import java.util.List;

public class IBookManagerImpl extends Binder implements IBookManager {


    public IBookManagerImpl() {
        attachInterface(this,IBookManager.DESCRIPTOR);
    }


    public static IBookManager asInterface(IBinder obj){

       IInterface iin = obj.queryLocalInterface(IBookManager.DESCRIPTOR);
       if(iin!=null && iin instanceof IBookManager){
           return ((IBookManager)iin);
       }
       //返回代理对象
       return null;
    }


    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {


        switch (code){
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }

            case TRANSACTION_getBookList:
                data.enforceInterface(DESCRIPTOR);
                List<Book> list = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(list);
                return true;

            case TRANSACTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if(data.readInt() !=0 ){
                    book = Book.CREATOR.createFromParcel(data);
                }else{
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code,data,reply,flags);

    }

    @Override
    public List<Book> getBookList() {
        //未实现
        return null;
    }

    @Override
    public void addBook(Book book) {
        //未实现
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    private static final class Proxy implements IBookManager{

        private IBinder mRemote;
        public Proxy(IBinder binder) {
            mRemote = binder;
        }

        public java.lang.String getInterfaceDescripor(){
            return DESCRIPTOR;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            List<Book>list = null;
            try {
                if(mRemote!=null){
                    data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getBookList,data,replay,0);
                    replay.readException();
                    list = replay.createTypedArrayList(Book.CREATOR);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                data.recycle();
                replay.recycle();
            }
            return list;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                if(book!=null){
                    data.writeInt(1);
                    book.writeToParcel(data,0);
                }else{
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook,data,replay,0);
                replay.readException();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                data.recycle();
                replay.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}

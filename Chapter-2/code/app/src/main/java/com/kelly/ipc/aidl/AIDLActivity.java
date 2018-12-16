package com.kelly.ipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kelly.ipc.Book;
import com.kelly.ipc.R;

import java.util.List;


public class AIDLActivity extends AppCompatActivity {

    private static final String TAG = AIDLActivity.class.getSimpleName();
    private com.kelly.ipc.IBookManager mBookManager;


    private Button mAddBtn;
    private Button mGetBtn;
    private TextView mShowTV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_layout);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        mGetBtn = (Button) findViewById(R.id.btn_get);
        mShowTV = (TextView) findViewById(R.id.show);
        Intent intent = new Intent(this,AIDLService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setAuthor("Goslin");
                book.setBookName("Java核心知识");
                try {
                    mBookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final List<Book> books = mBookManager.getListBook();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(books!=null && books.size()>0){
                                Log.i(TAG,"book.length:"+books.size());
                                StringBuffer buffer = new StringBuffer(10);
                                for (int i = 0; i < books.size(); i++) {
                                      buffer.append(books.get(i).toString()).append("\n");
                                }
                                if(buffer.length()>0){
                                    mShowTV.setText("");
                                }
                                mShowTV.setText(buffer.toString());
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"serviceConnected");
            mBookManager = com.kelly.ipc.IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"DisConnected");
        }
    };
}

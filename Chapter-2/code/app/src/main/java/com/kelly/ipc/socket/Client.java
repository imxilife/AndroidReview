package com.kelly.ipc.socket;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kelly.ipc.App;
import com.kelly.ipc.R;

public class Client extends AppCompatActivity {

    private Button mSend;
    private TextView mReceiver;
    private EditText mEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_layout);
        mSend = (Button) findViewById(R.id.send);
        mReceiver = (TextView) findViewById(R.id.receive);
        mEdit = (EditText) findViewById(R.id.edit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEdit.getText().toString();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

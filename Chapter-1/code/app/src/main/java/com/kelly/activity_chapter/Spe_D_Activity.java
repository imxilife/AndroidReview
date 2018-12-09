package com.kelly.activity_chapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Spe_D_Activity extends AppCompatActivity {


    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_layout);
        mTextView = (TextView) findViewById(R.id.tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextView.setText("我是 D");
    }
}

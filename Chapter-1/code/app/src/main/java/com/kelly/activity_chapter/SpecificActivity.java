package com.kelly.activity_chapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SpecificActivity extends AppCompatActivity {

    private Button mButtonA;
    private Button mButtonB;
    private Button mButtonC;
    private Button mButtonD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_layout);
        mButtonA = (Button) findViewById(R.id.a_btn);
        mButtonB = (Button) findViewById(R.id.b_btn);
        mButtonC = (Button) findViewById(R.id.c_btn);
        mButtonD = (Button) findViewById(R.id.d_btn);


        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecificActivity.this,Spe_A_Activity.class);
                startActivity(intent);
            }
        });

        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

package com.kelly.activity_chapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GetResultActivity extends AppCompatActivity {

    private static final String TAG = GetResultActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG," >>> test <<<");
                Intent intent = new Intent();
                intent.putExtra("Result","OK");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        },5000);
    }
}

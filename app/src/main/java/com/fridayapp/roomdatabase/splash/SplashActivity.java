package com.fridayapp.roomdatabase.splash;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fridayapp.roomdatabase.activities.MainActivity;
import com.fridayapp.roomdatabase.R;

public class SplashActivity extends Activity {


    private static int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
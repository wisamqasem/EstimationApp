package com.jdeco.estimationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.WindowManager;

import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.LoginUI;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        CharSequence doneDate = DateFormat.format("yyyy-MM-dd", new Date());
//        CharSequence dateBefore = DateFormat.format("yyyy-MM-dd", new Date());
//        CharSequence dateBefore = DateFormat.format("yyyy-MM-dd", (new Date(System.currentTimeMillis() - (365 * DAY_IN_MS))));
//        Boolean test = (doneDate.equals(dateBefore));
        // remove status bar from screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        session = new Session(SplashActivity.this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                session.checkLogin();

                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }
}
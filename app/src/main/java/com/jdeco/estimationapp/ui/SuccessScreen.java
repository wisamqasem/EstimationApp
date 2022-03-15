package com.jdeco.estimationapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.SplashActivity;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.forms.ApplicationsList;

public class SuccessScreen extends AppCompatActivity {
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove status bar from screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_success_screen);
        session = new Session(SuccessScreen.this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SuccessScreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
}
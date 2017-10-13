package com.example.gabriel.carona_fatec;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        }, 2000);

    }

    private void login(){

        Intent login = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(login);
        finish();

    }

}

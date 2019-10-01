package com.example.newpuzzlegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.*;

import androidx.appcompat.app.AppCompatActivity;

public class StartApp extends AppCompatActivity {
    String name;
    androidx.appcompat.widget.Toolbar toolbar_main;

    private static int TIME_OUT = 1000; //Time to launch the another activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(StartApp.this, Login.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);




    }

    public void playbtn(View v){
        Intent play = new Intent(StartApp.this, MainActivity.class);
        play.putExtra("name", name);
        startActivity(play);
        finish();
    }
}

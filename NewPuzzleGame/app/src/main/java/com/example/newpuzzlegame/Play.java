package com.example.newpuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Play extends AppCompatActivity {
    String name;
    int mode;
    androidx.appcompat.widget.Toolbar toolbar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mode = intent.getIntExtra("mode",0);


        if(mode == 1) {
            //toolbar setup
            toolbar_main = findViewById(R.id.toolbar_play);
            toolbar_main.setTitle(getString(R.string.relax));
            setSupportActionBar(toolbar_main);

        }else{
            toolbar_main = findViewById(R.id.toolbar_play);
            toolbar_main.setTitle(getString(R.string.challenge));
            setSupportActionBar(toolbar_main);
        }



    }

    public void playbtn(View v){
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        startActivity(play);
        finish();
    }
}

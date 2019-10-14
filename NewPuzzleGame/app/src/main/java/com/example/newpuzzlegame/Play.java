package com.example.newpuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Play extends AppCompatActivity {
    String name;
    int mode;
    int level;
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

    public void levelButton1(View v){
        level = 1;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton2(View v){
        level = 2;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton3(View v){
        level = 3;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton4(View v){
        level = 4;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton5(View v){
        level = 5;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton6(View v){
        level = 6;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
    public void levelButton0(View v){
        level = 0;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
        finish();
    }
//    @Override
//    public void onBackPressed() {
//        Intent play = new Intent(Play.this, Menu.class);
//        play.putExtra("name", name);
//        startActivity(play);
//        finish();
//    }
}

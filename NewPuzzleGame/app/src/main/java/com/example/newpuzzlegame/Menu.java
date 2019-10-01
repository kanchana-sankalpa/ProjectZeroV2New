package com.example.newpuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");


    }

    public void challangebtn(View v){

        Intent play = new Intent(Menu.this, Play.class);
        play.putExtra("name", name);
        play.putExtra("mode", 2);
        startActivity(play);
        finish();
    }

    public void relaxbtn(View v){

        Intent play = new Intent(Menu.this, Play.class);
        play.putExtra("name", name);
        play.putExtra("mode", 1);
        startActivity(play);
        finish();
    }

}

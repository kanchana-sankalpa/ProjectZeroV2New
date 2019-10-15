package com.example.newpuzzlegame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Play extends AppCompatActivity {
    String name;
    int mode;
    int level;
    androidx.appcompat.widget.Toolbar toolbar_main;
    String f_id;

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
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        f_id = sharedPreferences.getString("f_id","");
        for (int i=2;i <= 6;i++){
            getLevelIndicator(f_id,i);
        }




    }

    public void levelButton1(View v){
        level = 1;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton2(View v){
        level = 2;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton3(View v){
        level = 3;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton4(View v){
        level = 4;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton5(View v){
        level = 5;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton6(View v){
        level = 6;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    public void levelButton0(View v){
        level = 0;
        Intent play = new Intent(Play.this, MainActivity.class);
        play.putExtra("name", name);
        play.putExtra("mode", mode);
        play.putExtra("level",level);
        startActivity(play);
    }
    @Override
    public void onBackPressed() {
        Intent play = new Intent(this, Menu.class);
        startActivity(play);
        finish();
    }
    public void unLockLevel(int level){
        switch (level) {
            case 1:
                findViewById(R.id.level_button_1).setEnabled(true);
                findViewById(R.id.level_button_1).setBackgroundResource(R.drawable.buttonshape2);
                break;
            case 2:
                findViewById(R.id.level_button_2).setEnabled(true);
                findViewById(R.id.level_button_2).setBackgroundResource(R.drawable.buttonshape2);
                break;
            case 3:
                findViewById(R.id.level_button_3).setEnabled(true);
                findViewById(R.id.level_button_3).setBackgroundResource(R.drawable.buttonshape2);
                break;
            case 4:
                findViewById(R.id.level_button_4).setEnabled(true);
                findViewById(R.id.level_button_4).setBackgroundResource(R.drawable.buttonshape2);
                break;
            case 5:
                findViewById(R.id.level_button_5).setEnabled(true);
                findViewById(R.id.level_button_5).setBackgroundResource(R.drawable.buttonshape2);
                break;
            case 6:
                findViewById(R.id.level_button_6).setEnabled(true);
                findViewById(R.id.level_button_6).setBackgroundResource(R.drawable.buttonshape2);
                break;
        }
        Log.d("lock","lock level "+level);
    }
    public void getLevelIndicator(String f_id,int level){
        DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference().child("users").child(f_id).child("levels").child("level"+level);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean indicator = dataSnapshot.getValue(Boolean.class);

                if(indicator == null){
                    Log.d("lock","null,try to lock level "+level);
                }
                else if (indicator == true){
                    unLockLevel(level);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myz", "Error set score");

            }
        });
    }

}

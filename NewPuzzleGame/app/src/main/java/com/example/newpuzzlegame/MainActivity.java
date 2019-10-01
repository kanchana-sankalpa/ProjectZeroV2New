package com.example.newpuzzlegame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newpuzzlegame.model.Block;
import com.example.newpuzzlegame.view.Klotski;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    TextView tvname;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar_main;
    TextView timertxt;
    TextView stepstxt;
    public int seconds = 0;
    public int minutes = 0;
    Klotski mKlotskiView;
    AlertDialog alertDialog;
    LinearLayout dis;
String name;
    public Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepstxt = findViewById(R.id.steps);
        timertxt = findViewById(R.id.timer);
        dis = findViewById(R.id.dis);
        //Declare the timer
         t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(seconds<10) {
                            timertxt.setText(String.valueOf(minutes) + ":" + "0"+String.valueOf(seconds));
                        }else{
                            timertxt.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                        }
                        seconds += 1;

                        if(seconds == 60)
                        {
                            timertxt.setText(String.valueOf(minutes)+":"+String.valueOf(seconds));

                            seconds=0;
                            minutes=minutes+1;

                        }

                        if(minutes == 1){

                          mKlotskiView.openDialogtime2();
                          t.cancel();
                        }

                    }

                });
            }

        }, 0, 1000);


        Intent intent = getIntent();
       name = intent.getStringExtra("name");
       int mode = intent.getIntExtra("mode",0);
        Log.d("myz", "name :" + name);

        if(mode == 2){
            dis.setVisibility(View.VISIBLE);
        }else{
            dis.setVisibility(View.GONE);
        }

        //toolbar setup
        toolbar_main = findViewById(R.id.toolbar_main);
        toolbar_main.setTitle(name);
        setSupportActionBar(toolbar_main);



      //  List<Block> blocks = KlotskiMapParser.parse("2,0,0,4,1,0,2,3,0,2,0,2,3,1,2,2,3,2,1,1,3,1,2,3,1,0,4,1,3,4");
        List<Block> blocks = KlotskiMapParser.parse("1,0,0,4,1,0,1,3,0,1,0,1,1,1,2,1,2,2,1,3,1,2,0,2,1,1,3,1,2,3,2,3,2,1,0,4,1,3,4");

         mKlotskiView = findViewById(R.id.main_klotski);
        mKlotskiView.setBlocks(blocks);
    }

    @Override
    public void onBackPressed() {
        Intent play = new Intent(MainActivity.this, Menu.class);
        play.putExtra("name", name);
        startActivity(play);
        finish();
    }
}


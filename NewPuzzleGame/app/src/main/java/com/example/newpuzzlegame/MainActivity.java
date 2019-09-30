package com.example.newpuzzlegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newpuzzlegame.model.Block;
import com.example.newpuzzlegame.view.Klotski;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;



public class MainActivity extends AppCompatActivity {

    TextView tvname;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
       String name = intent.getStringExtra("name");

        Log.d("myz", "name :" + name);

        //toolbar setup
        toolbar_main = findViewById(R.id.toolbar_main);
        toolbar_main.setTitle(name);
        setSupportActionBar(toolbar_main);



        List<Block> blocks = KlotskiMapParser.parse("2,0,0,4,1,0,2,3,0,2,0,2,3,1,2,2,3,2,1,1,3,1,2,3,1,0,4,1,3,4");

        Klotski mKlotskiView = findViewById(R.id.main_klotski);
        mKlotskiView.setBlocks(blocks);
    }


}


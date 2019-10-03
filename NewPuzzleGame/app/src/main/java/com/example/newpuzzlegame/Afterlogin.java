package com.example.newpuzzlegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Afterlogin extends AppCompatActivity {
    String name;
    TextView user_name;
    Button signout;
    String f_id;
    DatabaseReference myRef;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);

        Intent intent = getIntent();
         name = intent.getStringExtra("name");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.getRef();

        SharedPreferences mSettings = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        f_id = mSettings.getString("f_id","");

        signout = findViewById(R.id.signout);
        user_name = findViewById(R.id.name);

        user_name.setText(name);
    }


    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void login(View v) {

        Intent username = new Intent(Afterlogin.this, Menu.class);
        username.putExtra("name", name);
        startActivity(username);
        finish();
    }

    }
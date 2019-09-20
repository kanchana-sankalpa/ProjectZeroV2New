package com.example.newpuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class UserName extends AppCompatActivity {
    Button signout;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        signout = findViewById(R.id.signout);
        name = findViewById(R.id.name);
    }


    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void login(View v){
        Intent username = new Intent(UserName.this, MainActivity.class);
        username.putExtra("name", name.getText().toString());
        startActivity(username);
        finish();
    }

}

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserName extends AppCompatActivity {
    Button signout;
    EditText name;
    DatabaseReference myRef;
    private DatabaseReference mDatabase;
    String f_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.getRef();

        SharedPreferences mSettings = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        f_id = mSettings.getString("f_id","");

        signout = findViewById(R.id.signout);
        name = findViewById(R.id.name);
    }


    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void login(View v){

        String uname = name.getText().toString();

        Log.d("myz", "F-ID  :"+f_id);
        if(!uname.equals("")){
        myRef.child("users").child(f_id).child("user_name").setValue(uname)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("myz", "Success");

                        SharedPreferences sharedPref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("uname", uname);
                        editor.apply();

                        Intent username = new Intent(UserName.this, Menu.class);
                        username.putExtra("name", name.getText().toString());
                        startActivity(username);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("myz", "Failed");
                    }
                });



    }else {
            Toast.makeText(this, ""+getString(R.string.name_please), Toast.LENGTH_SHORT).show();
        }

    }

}

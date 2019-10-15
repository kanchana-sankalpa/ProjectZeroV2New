package com.example.newpuzzlegame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Db {
    DatabaseReference dbReference;

    public void setLevelIndicator(String f_id,int level,Boolean Indicator){
        dbReference = FirebaseDatabase.getInstance().getReference();
        dbReference.child("users").child(f_id).child("levels").child("level"+level).setValue(Indicator)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}

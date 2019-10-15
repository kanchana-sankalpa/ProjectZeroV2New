package com.example.newpuzzlegame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

//    public boolean getLevelIndicator(String f_id,int level){
//        dbReference = FirebaseDatabase.getInstance().getReference().child("users").child(f_id).child("levels").child("level"+level);
//        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Boolean indicator = dataSnapshot.getValue(Boolean.class);
//
//                if(indicator != true){
//                    return true;
//                }else {
//                    onlineScore= new Long(scor).intValue();
//                }
//                openDialogtime();
//
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("myz", "Error set score");
//
//            }
//        });
//    }
}

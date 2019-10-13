package com.example.newpuzzlegame;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.MoreObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rank extends AppCompatActivity {
    DatabaseReference myref,myrefnew;
    String f_id;
    ArrayList<User> list = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getAllUserScore();
}
    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    public void getAllUserScore(){
        myref = FirebaseDatabase.getInstance().getReference().child("users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int score = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("score").getValue(Long.class)!=null){
                        score = ds.child("score").getValue(Long.class).intValue();
                    }
                    else
                    {
                        score = 0;
                    }
                    if (score !=0){
                        list.add(new User(ds.child("name").getValue(String.class), score));
                    }
                }
                Collections.sort(list);
                ArrayList<String> listToShow = new ArrayList<String>();
                if (list.size()>9){
                    for (int i = list.size()-1;i>=(list.size()-9);i--){
                        listToShow.add(list.get(i).getUserName()+"------------"+list.get(i).getScore());
                    }
                }
                else{
                    for (int i = list.size()-1;i>=0;i--){
                        listToShow.add(list.get(i).getUserName()+"------------"+list.get(i).getScore());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Rank.this,android.R.layout.simple_list_item_1,listToShow);
                ListView rank = findViewById(R.id.rank);
                rank.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myz", "Error set score");

            }
        });
    }

}

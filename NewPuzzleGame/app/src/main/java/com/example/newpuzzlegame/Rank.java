package com.example.newpuzzlegame;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rank extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ArrayList<User> list = new ArrayList<User>();
        list.add(new User("1","Joshua",22));
        list.add(new User("2","Kanchana",44));
        list.add(new User("3","Anil",33));
        Collections.sort(list);
        ArrayList<String> listToShow = new ArrayList<String>();
        for (int i = list.size()-1;i>=0;i--){
            listToShow.add(list.get(i).getUserName()+"------------"+list.get(i).getScore());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listToShow);
        ListView rank = findViewById(R.id.rank);
        rank.setAdapter(adapter);
}

public void ReadData(){

}
}

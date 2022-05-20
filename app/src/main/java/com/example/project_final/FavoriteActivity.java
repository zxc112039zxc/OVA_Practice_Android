package com.example.project_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;
    ArrayList<Problem> favoriteArrayList = new ArrayList<>();
    DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        RecyclerView favoriteRecyclerView = (RecyclerView) findViewById(R.id.favoriteRecyclerView);
        mDBHelper = new DBHelper(this);
        favoriteArrayList = mDBHelper.getProblemToProblemArrayList();

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(favoriteArrayList);
        favoriteRecyclerView.setAdapter(favoriteRecyclerViewAdapter);

        favoriteRecyclerViewAdapter.getFilter().filter("favorite");
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
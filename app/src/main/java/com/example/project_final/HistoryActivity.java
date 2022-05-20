package com.example.project_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    ArrayList<Problem> historyArrayList = new ArrayList<>();
    DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView historyRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        mDBHelper = new DBHelper(this);
        historyArrayList = mDBHelper.getHistoryToArrayList();

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyArrayList);
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
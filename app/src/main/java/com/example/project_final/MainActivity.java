package com.example.project_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    User user = new User();
    SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    RecentRecyclerViewAdapter recentRecyclerViewAdapter;
    ArrayList<Problem> problemArrayList, historyArrayList = new ArrayList<>();
    View homeView, dailyProblemView;
    TextView tvDailyNumber, tvDailyProblem;
    SearchView searchView;
    DBHelper mDBHelper;
    int randomIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        for (int i = 1; i <= 3; i++)
//        {
//            ArrayList<String> problemInfo = new ArrayList<>();
//            problemInfo.add("#" + i);
//            problemInfo.add("Problem" + i);
//            problemArrayList.add(problemInfo);
//        }

        RecyclerView searchRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        RecyclerView recentRecyclerView = (RecyclerView) findViewById(R.id.recentRecyclerView);
        tvDailyNumber = (TextView) findViewById(R.id.tvDailyNumber);
        tvDailyProblem = (TextView) findViewById(R.id.tvDailyProblem);
        dailyProblemView = (View) findViewById(R.id.dailyProblemView);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDBHelper = new DBHelper(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        homeView = findViewById(R.id.homeView);
        problemArrayList = mDBHelper.getProblemToProblemArrayList();
        historyArrayList = mDBHelper.getHistoryToArrayList();

        setSupportActionBar(toolbar);

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(problemArrayList);
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
//        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchRecyclerView.setVisibility(View.INVISIBLE);

        recentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentRecyclerViewAdapter = new RecentRecyclerViewAdapter(historyArrayList);
        recentRecyclerView.setAdapter(recentRecyclerViewAdapter);
        recentRecyclerViewAdapter.getFilter().filter("recent");
        recentRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (randomIndex == -1) {
            randomIndex = new Random().nextInt(problemArrayList.size());
        }
        tvDailyNumber.setText(problemArrayList.get(randomIndex).getProblemNumber());
        tvDailyProblem.setText(problemArrayList.get(randomIndex).getProblemName());

        Bundle userInfoBundle = getIntent().getExtras();
        if (userInfoBundle != null)
        {
            user.setUsername(userInfoBundle.getString("username"));
            user.setLoggedIn(userInfoBundle.getBoolean("loggedIn"));
        }

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0)
                {
                    searchRecyclerView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    searchRecyclerView.setVisibility(View.VISIBLE);
                }
                searchRecyclerViewAdapter.getFilter().filter(s);

                return false;
            }
        });

        dailyProblemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("problemName", problemArrayList.get(randomIndex).getProblemName());
                startActivity(new Intent(MainActivity.this, ProblemActivity.class).putExtras(bundle));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);

                int id = item.getItemId();

                if (id == R.id.login)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("username",user.getUsername());
                    bundle.putBoolean("loggedIn", user.isLoggedIn());
                    startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtras(bundle));

                    return true;
                }
                else if (id == R.id.favorite)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("username",user.getUsername());
                    bundle.putBoolean("loggedIn", user.isLoggedIn());
                    startActivity(new Intent(MainActivity.this, FavoriteActivity.class).putExtras(bundle));

                    return true;
                }
                else if (id == R.id.history)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("username",user.getUsername());
                    bundle.putBoolean("loggedIn", user.isLoggedIn());
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class).putExtras(bundle));

                    return true;
                }
                else if (id == R.id.logout)
                {
                    if (user.logout())
                    {
                        Toast.makeText(MainActivity.this, "Log out successfully!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not logged in!", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
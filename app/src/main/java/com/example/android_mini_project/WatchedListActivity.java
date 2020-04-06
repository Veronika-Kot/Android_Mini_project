package com.example.android_mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.view.LayoutInflater;

import static android.content.Context.*;

import com.example.android_mini_project.models.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WatchedListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DatabaseReference movieDatabase;
    private WatchListAdapter watchListAdapter;
    ListView watchList;
    Button buttonSortDate;
    Button buttonSortTitle;
    private String sortBy;
    ValueEventListener getListener;
    BottomNavigationView bottomNavigationView;

    Menu menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_list);
        // setting navigation bar
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.activity_main, null, false);
//        drawerLayout.addView(contentView, 0);
//
//        drawerLayout=findViewById(R.id.drawer_layout);
//        navigationView=findViewById(R.id.nav_view);
////        toolbar=findViewById(R.id.toolbar);
//
//        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.watched_list);

//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#FFD300"));
//
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        navigationView.setCheckedItem(R.id.nav_watch_list);


        // Navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_watched_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_watch_list:
                        Intent intent = new Intent(WatchedListActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_movielist:
                        Intent intent2 = new Intent(WatchedListActivity.this,MovieListActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_watched_list:
                        break;
                }
                return false;
            }
        });


        // Populate watch list
        sortBy = "byDate";
        buttonSortDate = findViewById(R.id.buttonSortDate);
        buttonSortTitle = findViewById(R.id.buttonSortTitle);
        buttonSortDate.setSelected(true);
        buttonSortTitle.setSelected(false);
        watchList = findViewById(R.id.watchList);
        watchListAdapter  = new WatchListAdapter(WatchedListActivity.this, R.layout.watch_list_rou_layout);
        watchList.setAdapter(watchListAdapter);

        movieDatabase = FirebaseDatabase.getInstance().getReference();
        getListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                watchListAdapter.clearList();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("watched").getValue().toString() == "true"){
                        watchListAdapter.add(ds.getValue(Movie.class));
                    }
                }
                switch(sortBy) {
                    case "byDateReverse":
                        watchListAdapter.sortByDateReverse();
                        break;
                    case "byTitle":
                        watchListAdapter.sortByTitle();
                        break;
                    case "byTitleReverse":
                        watchListAdapter.sortByTitleReverse();
                        break;
                    default:
                        watchListAdapter.sortByDate();
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        movieDatabase.addValueEventListener(getListener);
    }

    public void sortByDate(View view){
        buttonSortDate.setSelected(true);
        buttonSortTitle.setSelected(false);
        if (sortBy == "byDate"){
            watchListAdapter.sortByDateReverse();
            sortBy = "byDateReverse";
        }
        else{
            watchListAdapter.sortByDate();
            sortBy = "byDate";
        }
        watchListAdapter.notifyDataSetChanged();
    }

    public void sortByTitle(View view){
        buttonSortDate.setSelected(false);
        buttonSortTitle.setSelected(true);
        if (sortBy == "byTitle"){
            watchListAdapter.sortByTitleReverse();
            sortBy = "byTitleReverse";
        }
        else{
            watchListAdapter.sortByTitle();
            sortBy = "byTitle";
        }
        watchListAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onBackPressed(){
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }
//        else
//        {super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(toggle.onOptionsItemSelected(item))
//        {return  true;}
        switch (item.getItemId()) {
            case R.id.search_movies:
                Intent intent = new Intent(WatchedListActivity.this, MovieListActivity.class);
                startActivity(intent);
                return true;
            default:

                return true;
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_watch_list: break;
            case R.id.nav_watched_list:
                Intent intent = new Intent(WatchedListActivity.this,WatchedListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_movielist:
                Intent intent1 = new Intent(WatchedListActivity.this,MovieListActivity.class);
                startActivity(intent1);
                break;

        }
//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieDatabase.removeEventListener(getListener);
    }
}

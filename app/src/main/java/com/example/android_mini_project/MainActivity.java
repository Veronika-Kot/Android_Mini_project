package com.example.android_mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;



import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    Menu menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
//        toolbar=findViewById(R.id.toolbar);

        this.setTitle(R.string.watch_list);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#FFD300"));
//
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_watch_list);
    }


    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(toggle.onOptionsItemSelected(item))
       {return  true;}
        switch (item.getItemId()) {
            case R.id.search_movies:
                Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
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
                Intent intent = new Intent(MainActivity.this,WatchedListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_movielist:
                Intent intent1 = new Intent(MainActivity.this,MovieListActivity.class);
                startActivity(intent1);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }}


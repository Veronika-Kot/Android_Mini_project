package com.example.android_mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import com.example.android_mini_project.helpers.PosterDownloader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.android_mini_project.models.Movie;

public class Movie_Description extends MainActivity {

    TextView descriptionTitle;
    TextView descriptionDescription;
    ImageView descriptionImageView;
    TextView descriptionReleaseDate;
    EditText descriptionEditNotes;
    DatabaseReference movieDatabase;
    BottomNavigationView bottomNavigationView;

    Integer id;
    String title;
    String poster_path;
    String overview;
    String release_date;
    String notes;
    Boolean watched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie__description);
        this.setTitle(R.string.movieDescription);

        // Navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_watch_list:
                        Intent intent = new Intent(Movie_Description.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_movielist:
                        Intent intent2 = new Intent(Movie_Description.this,MovieListActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_watched_list:
                        Intent intent3 = new Intent(Movie_Description.this,WatchedListActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

        descriptionTitle = findViewById(R.id.descriptionTitle);
        descriptionDescription = findViewById(R.id.descriptionDescription);
        descriptionImageView = findViewById(R.id.descriptionImageView);
        descriptionReleaseDate = findViewById(R.id.descriptionDate);
        descriptionEditNotes = findViewById(R.id.descriptionEditNotes);
        movieDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle b = getIntent().getExtras();
        id = b.getInt("id");
        title = b.getString("title");
        poster_path = b.getString("poster_path");
        overview = b.getString("overview");
        release_date = b.getString("release_date");
        notes = b.getString("notes");
        watched = b.getBoolean("watched");

        if (poster_path != null) {
            // show The Image in a ImageView
            new PosterDownloader(descriptionImageView)
                    .execute("http://image.tmdb.org/t/p/w185/" + poster_path);

        }
        descriptionTitle.setText("Title: " + title);
        descriptionDescription.setText("Overview: " + overview);
        descriptionReleaseDate.setText("Release Date: " + release_date);
        descriptionEditNotes.setText(notes);
    }

    public void updateNotes(View view){
        notes = descriptionEditNotes.getText().toString();
        movieDatabase.child(id + "").child("notes").setValue(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Notes Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void backToList(View view){
        if (watched){
            Intent i = new Intent(Movie_Description.this, WatchedListActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(Movie_Description.this, MainActivity.class);
            startActivity(i);
        }
    }
}

package com.example.android_mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Movie_Description extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle(R.string.movieDescription);
        TextView MovieTitle = (TextView) findViewById(R.id.movie_title);
        TextView Description = (TextView) findViewById(R.id.movie_description);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__description);
    }
}

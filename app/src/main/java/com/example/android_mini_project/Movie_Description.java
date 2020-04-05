package com.example.android_mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.android_mini_project.models.Movie;

public class Movie_Description extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle(R.string.movieDescription);
        TextView MovieTitle = (TextView) findViewById(R.id.movie_title);
        TextView Description = (TextView) findViewById(R.id.movie_description);
        TextView Year = (TextView) findViewById(R.id.movie_release_year);


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie__description);




        }
}

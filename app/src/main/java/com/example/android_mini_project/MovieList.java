package com.example.android_mini_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android_mini_project.viewmodels.MovieViewModal;

public class MovieList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Setting title to the Activity
        this.setTitle(R.string.search_movies);

        // Creates reference of ListView
        final ListView listview = findViewById(R.id.movieList);


        MovieViewModal model = ViewModelProviders.of(this).get(MovieViewModal.class);

        model.getMovieList().observe(this, patientlist -> {
            MovieArrayAdapter adapter = new MovieArrayAdapter(MovieList.this, patientlist);
            listview.setAdapter(adapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                Intent intent = new Intent(MovieList.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }
}

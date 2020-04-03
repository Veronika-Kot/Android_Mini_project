package com.example.android_mini_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.android_mini_project.helpers.Globals;
import com.example.android_mini_project.models.Movie;
import com.example.android_mini_project.viewmodels.MovieViewModal;

public class MovieList extends AppCompatActivity {

    MovieApiListAdapter adapter;
    MovieViewModal itemViewModel;
    RecyclerView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Setting title to the Activity
        this.setTitle(R.string.search_movies);

        // Creates reference of ListView
        listview = findViewById(R.id.movieList);

        adapter = new MovieApiListAdapter();
        listview.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = ViewModelProviders.of(this).get(MovieViewModal.class);

        itemViewModel.movieList.observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                adapter.submitList(movies);
            }
        });
        listview.setAdapter(adapter);

        findViewById(R.id.popular).setSelected(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                Globals.endPoint = "search/movie";
                Globals.searchQuery = query;
                itemViewModel.invalidateDataSource();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                Intent intent = new Intent(MovieList.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_by_photo:
                Log.i("!!!@@@!!!", "Search by photo");
                return true;
            default:
                return true;
        }
    }

    public void selectMovieList(View v) {
        Log.d("!!!!@@@!!!!", itemViewModel.movieList.getValue().get(1).getTitle());

        switch (v.getId()) {
            case R.id.upcoming:
                Globals.endPoint = "movie/upcoming";
                unselectButtons();
                v.setSelected(true);
                break;
            case R.id.popular:
                Globals.endPoint = "movie/popular";
                unselectButtons();
                v.setSelected(true);
                break;
            case R.id.top_rated:
                Globals.endPoint = "movie/top_rated";
                unselectButtons();
                v.setSelected(true);
                break;
            case R.id.now_playing:
                Globals.endPoint = "movie/now_playing";
                unselectButtons();
                v.setSelected(true);
                break;
        }

        listview.setVisibility(View.INVISIBLE);
        itemViewModel.invalidateDataSource();
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
        listview.setVisibility(View.VISIBLE);
    }

    private void unselectButtons() {
        findViewById(R.id.popular).setSelected(false);
        findViewById(R.id.upcoming).setSelected(false);
        findViewById(R.id.top_rated).setSelected(false);
        findViewById(R.id.now_playing).setSelected(false);
    }
}

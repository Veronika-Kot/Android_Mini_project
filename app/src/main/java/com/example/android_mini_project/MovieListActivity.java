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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.android_mini_project.helpers.Globals;
import com.example.android_mini_project.models.Movie;
import com.example.android_mini_project.viewmodels.MovieViewModal;
import com.google.android.gms.common.api.CommonStatusCodes;

/**
 * MovieListActivity to show a list of all available movies
 */
public class MovieListActivity extends MainActivity {

    MovieApiListAdapter adapter;
    MovieViewModal itemViewModel;
    RecyclerView listview;
    SearchView searchView;

    private static final String TAG = "MovieListActivity";

    //For ImageToText
    private static final int RC_OCR_CAPTURE = 9003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // setting navigation bar
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);


        // Setting title to the Activity
        this.setTitle(R.string.search_movies);

        // Creates reference of RecyclerView
        listview = findViewById(R.id.movieList);

        // Creating and setting adapter
        adapter = new MovieApiListAdapter();
        listview.setLayoutManager(new LinearLayoutManager(this));

        // Getting instance of MovieViewModal
        itemViewModel = ViewModelProviders.of(this).get(MovieViewModal.class);

        // Observing the datasource change
        itemViewModel.movieList.observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                adapter.submitList(movies);
            }
        });
        listview.setAdapter(adapter);

        // Setting Popular button to be presses, since it's a default endpoint
        findViewById(R.id.popular).setSelected(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);




        // Configuring  search view
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchMenuItem.getActionView();


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
                Intent intent = new Intent(MovieListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_by_photo:
                Log.i("!!!@@@!!!", "Search by photo");
                // launch Ocr capture activity.
                Intent i = new Intent(this, OcrCaptureActivity.class);
                i.putExtra(OcrCaptureActivity.AutoFocus, true);
                i.putExtra(OcrCaptureActivity.UseFlash, false);

                startActivityForResult(i, RC_OCR_CAPTURE);
                return true;
            default:
                return true;
        }
    }

    /**
     * selectMovieList - called by carousel of buttons,
     * it's changes the global end point variable and invalidates data source
     * so new API call is made
     *
     * @param v
     */
    public void selectMovieList(View v) {
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

        // Adding a slight delay to prevent list of movies flicking
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
        listview.setVisibility(View.VISIBLE);
    }

    /**
     *  unselectButtons - unselecting all buttons in the carousel
     */
    private void unselectButtons() {
        findViewById(R.id.popular).setSelected(false);
        findViewById(R.id.upcoming).setSelected(false);
        findViewById(R.id.top_rated).setSelected(false);
        findViewById(R.id.now_playing).setSelected(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);

                    searchView.setQuery(text, true);

                    Log.d(TAG, "Text read: " + text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                Log.d(TAG, "No Text captured, something went wrong...!!!");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

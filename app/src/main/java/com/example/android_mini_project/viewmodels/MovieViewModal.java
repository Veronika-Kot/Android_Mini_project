package com.example.android_mini_project.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_mini_project.models.Movie;
import com.example.android_mini_project.models.MovieResults;
import com.example.android_mini_project.server.GsonRequest;
import com.example.android_mini_project.server.ServerRequestQueue;


import java.util.ArrayList;
import java.util.List;

public class MovieViewModal extends AndroidViewModel {
    public static final String TAG = "MovieList";
    private MutableLiveData<List<Movie>> mMovieList;
    private String api_key = "45f9a064efe7ae481390491ab004cfef";

    private MovieResults mMovieResult;

    public MovieViewModal(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Movie>>  getMovieList() {
        if (mMovieList == null) {
            mMovieList = new MutableLiveData<>();
            loadMovies();
        }
        return mMovieList;
    }

    /**
     * ASYNC function to load Movies data from the tmdb api,
     * using volley's GSON request
     */
    private void loadMovies() {

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+api_key+"&language=en-US";

        GsonRequest<MovieResults> moviesRequest = new GsonRequest<MovieResults>(url, MovieResults.class, null, new Response.Listener<MovieResults>() {
            @Override
            public void onResponse(MovieResults response) {
                mMovieList.setValue(response.getResults());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "No RESPONSE");
                Log.e("ERROR", error.getMessage());
            }
        }) {

        };

        // Set the tag on the request.
        moviesRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        ServerRequestQueue.getInstance(getApplication().getApplicationContext()).addToRequestQueue(moviesRequest);

    }
}


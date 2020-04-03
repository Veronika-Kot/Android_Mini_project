package com.example.android_mini_project.models;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.android_mini_project.helpers.Globals;
import com.example.android_mini_project.server.ApiService;
import com.example.android_mini_project.server.ApiServiceBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource  extends PageKeyedDataSource<Long, Movie> {
    public static long FIRST_PAGE = 1;
    public static int COUNT_ON_PAGE = 20;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
    // Making an API call for the first page of movies

            ApiService service = ApiServiceBuilder.buildService(ApiService.class);
            Call<MovieResponse> call = service.getNowPlaying(Globals.endPoint, FIRST_PAGE, Globals.searchQuery);
            call.enqueue(new Callback<MovieResponse>() {
                @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    MovieResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        List<Movie> responseItems = apiResponse.getResults();
                        callback.onResult(responseItems, null, FIRST_PAGE + 1);
                    }
                }
                @Override public void onFailure(Call<MovieResponse> call, Throwable t) {
                }
            });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
    // Making an API call for next page of movies

        ApiService service = ApiServiceBuilder.buildService(ApiService.class);
        Call<MovieResponse> call = service.getNowPlaying(Globals.endPoint, params.key, Globals.searchQuery);
        call.enqueue(new Callback<MovieResponse>() {
            @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse apiResponse = response.body();
                if (apiResponse != null) {
                    List<Movie> responseItems = apiResponse.getResults();
                    callback.onResult(responseItems, params.key + 1);
                }
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {
            }
        });
    }
}

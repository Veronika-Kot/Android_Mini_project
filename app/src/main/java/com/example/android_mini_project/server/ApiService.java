package com.example.android_mini_project.server;

import com.example.android_mini_project.models.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("{endPoint}?api_key=45f9a064efe7ae481390491ab004cfef&language=en-US")
    Call<MovieResponse> getNowPlaying(@Path(value="endPoint", encoded=true) String endPoint,
                                      @Query("page") long page,
                                      @Query("query") String query);


}
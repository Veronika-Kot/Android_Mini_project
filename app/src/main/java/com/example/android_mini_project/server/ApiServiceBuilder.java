package com.example.android_mini_project.server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiServiceBuilder {
    // Base URL
    private static final String URL = "https://api.themoviedb.org/3/";

    // Create Logger
    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder okHttp = new OkHttpClient.Builder().addInterceptor(logger);

    // Create Retrofit Builder
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    // Create Retrofit Instance
    private static Retrofit retrofit = builder.build();

    /**
     * buildService - to build a requested service
     * @param type
     * @param <T>
     * @return retrofit service
     */
    public static <T> T buildService(Class<T> type) {
        return retrofit.create(type);
    }
}
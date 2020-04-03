package com.example.android_mini_project.models;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory<Long, Movie> {
    public MutableLiveData<MovieDataSource> movieLiveDataSource=new MutableLiveData<>();

    @Override public DataSource<Long, Movie> create() {
        MovieDataSource userDataSource = new MovieDataSource();
        movieLiveDataSource.postValue(userDataSource);
        return userDataSource;
    }
}

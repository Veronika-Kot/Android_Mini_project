package com.example.android_mini_project.viewmodels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.example.android_mini_project.models.Movie;
import com.example.android_mini_project.models.MovieDataSource;
import com.example.android_mini_project.models.MovieDataSourceFactory;


public class MovieViewModal extends ViewModel {

    public LiveData<PagedList<Movie>> movieList;
    private LiveData<MovieDataSource> liveDataSource;

    public MovieViewModal() { init(); }

    /**
     * init - setting PagedList configurations, getting data
     */
    private void init() {
        MovieDataSourceFactory itemDataSourceFactory = new MovieDataSourceFactory();
        liveDataSource = itemDataSourceFactory.movieLiveDataSource;

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(MovieDataSource.COUNT_ON_PAGE)
                .build();
        movieList = new LivePagedListBuilder<>(itemDataSourceFactory, config).build();
    }

    /**
     * invalidateDataSource - makes data source invalid, so new API call is made
     */
    public void invalidateDataSource() {
        liveDataSource.getValue().invalidate();
    }
}


package com.example.android_mini_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mini_project.models.Movie;

import java.io.InputStream;

public class MovieApiListAdapter extends PagedListAdapter<Movie, MovieApiListAdapter.MovieViewHolder> {

    public MovieApiListAdapter() {
        super(MOVIE_COMPARATOR);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_rowlayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    //Class to connect row UI elements with data
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewRating;
        private TextView textViewData;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewRating = (TextView) itemView.findViewById(R.id.rating);
            textViewData = (TextView) itemView.findViewById(R.id.data);
        }

        void bind(Movie movie) {
            //Assigning Movies data to UI elements
            textViewTitle.setText(movie.getTitle());
            textViewRating.setText(movie.getRelease_date());
            textViewData.setText(movie.getOverview());

            //Assigning Movie Poster
            if (movie.getPoster_path() != null) {
                // show The Image in a ImageView
                new MovieViewHolder.DownloadPosterTask((ImageView) itemView.findViewById(R.id.appCompatImageView))
                        .execute("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());

            }
        }

        /**
         * ASYNC task to load movie poster
         */
        private class DownloadPosterTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadPosterTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    private static final DiffUtil.ItemCallback<Movie> MOVIE_COMPARATOR = new DiffUtil.ItemCallback<Movie>() {
        @Override public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @SuppressLint("DiffUtilEquals")
        @Override public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem == newItem;
        }
    };
}
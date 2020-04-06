package com.example.android_mini_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mini_project.helpers.PosterDownloader;
import com.example.android_mini_project.models.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MovieApiListAdapter extends PagedListAdapter<Movie, MovieApiListAdapter.MovieViewHolder> {

    private List<Integer> listOfMovieIds;
    /**
     * MovieApiListAdapter for a RecyclerView
     */
    public MovieApiListAdapter(List<Integer> movieIdList) {

        super(MOVIE_COMPARATOR);
        listOfMovieIds = movieIdList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_rowlayout, parent, false);
        return new MovieViewHolder(view, listOfMovieIds);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(getItem(position));

        // This listener executed for each list't item
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            // This function is executed when item is clicked
            @Override
            public void onClick(View v) {
               Log.i("!!!@@@!!!", "I've selected a row " + getItem(position).getTitle());
            }
        });
    }

    /**
     * MovieViewHolder class to represent a single row (RecyclerView)
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewYear;
        private TextView textViewData;
        private ImageButton buttonFavorite;
        private DatabaseReference movieDatabase;
        private List<Integer> movieIdList;

        // Constructor
        MovieViewHolder(@NonNull View itemView, List<Integer> listOfMovieIds) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewYear = (TextView) itemView.findViewById(R.id.year);
            textViewData = (TextView) itemView.findViewById(R.id.data);
            buttonFavorite = (ImageButton) itemView.findViewById(R.id.like);
            movieDatabase = FirebaseDatabase.getInstance().getReference();
            movieIdList = listOfMovieIds;
        }

        /**
         * bind -  to connect the row view with data
         * @param movie
         */
        void bind(Movie movie) {
            //Assigning Movies data to UI elements
            textViewTitle.setText(movie.getTitle());
            textViewYear.setText(movie.getRelease_date());
            textViewData.setText(movie.getOverview());

            //Assigning Movie Poster
            if (movie.getPoster_path() != null) {
                // show The Image in a ImageView
                new PosterDownloader((ImageView) itemView.findViewById(R.id.appCompatImageView))
                        .execute("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());

            }

            if (movieIdList.contains(movie.getId())){
                buttonFavorite.setSelected(true);
            }

            // Listender for a button inside the item row
            buttonFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()){
                        movieDatabase.child(movie.getId() + "").removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        v.setSelected(false);
                                        Toast.makeText(itemView.getContext(), "Removed From Watch List", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        v.setSelected(true);
                                        Toast.makeText(itemView.getContext(), "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{
                        Movie newMovie = new Movie(movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getOverview(), movie.getPoster_path());
                        movieDatabase.child(movie.getId() + "").setValue(newMovie)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        v.setSelected(true);
                                        Toast.makeText(itemView.getContext(), "Added To Watch List", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        v.setSelected(false);
                                        Toast.makeText(itemView.getContext(), "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                }
            });
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
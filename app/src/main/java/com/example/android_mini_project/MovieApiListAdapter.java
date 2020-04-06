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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android_mini_project.helpers.PosterDownloader;
import com.example.android_mini_project.models.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            buttonFavorite.setSelected(movieIdList.contains(movie.getId()));

            // Listender for a button inside the item row
            buttonFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()){
                        movieIdList.remove(Integer.valueOf(movie.getId()));
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
                        movieIdList.add(movie.getId());
                        Movie newMovie = new Movie(movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getOverview(), movie.getPoster_path());
                        movieDatabase.child(movie.getId() + "").setValue(newMovie)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        v.setSelected(true);
                                        Toast.makeText(itemView.getContext(), "Added To Watch List", Toast.LENGTH_LONG).show();
                                        //send e-mail

                                        sendEmail();

                                    }
                                    public void sendEmail() {
                                        RequestQueue queue;
                                        queue = Volley.newRequestQueue(v.getContext());
                                        String url = "https://jasontestformapd.us-south.cf.appdomain.cloud/m";
                                        String emailAddress = "jasonsun0603@gmail.com";
                                        Map<String, String> params = new HashMap();
                                        params.put("movie_title", movie.getTitle());
                                        params.put("movie_release", movie.getRelease_date());
                                        params.put("movie_overview", movie.getOverview());
                                        params.put("movie_poster", movie.getPoster_path());
                                        params.put("email_address", emailAddress);
                                        params.put("day", "5");

                                        JSONObject parameters = new JSONObject(params);

                                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                //
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                //
                                            }
                                        });
                                        queue.add(jsonRequest);
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
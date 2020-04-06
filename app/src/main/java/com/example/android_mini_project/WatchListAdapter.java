package com.example.android_mini_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.android_mini_project.helpers.PosterDownloader;
import com.example.android_mini_project.models.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WatchListAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = "WatchListAdapter";
    private List<Movie> movieList;
    private Context cxt;
    private DatabaseReference movieDatabase;

    static class WatchListViewHolder {
        TextView textViewTitle;
        TextView textViewDate;
        ImageButton buttonWatch;
        ImageButton buttonEdit;
        ImageView imageViewMoviePoster;
    }

    public WatchListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        cxt = context;
        movieDatabase = FirebaseDatabase.getInstance().getReference();
        movieList = new ArrayList<Movie>();
    }

    @Override
    public void add(Movie movie) {
        movieList.add(movie);
        super.add(movie);
    }

    @Override
    public int getCount() {
        return this.movieList.size();
    }

    @Override
    public Movie getItem(int index) {
        return this.movieList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WatchListViewHolder viewHolder;
        Movie movie = getItem(position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.watch_list_rou_layout, parent, false);
            viewHolder = new WatchListViewHolder();
            viewHolder.textViewTitle = row.findViewById(R.id.title2);
            viewHolder.textViewDate = row.findViewById(R.id.releaseDate);
            viewHolder.buttonWatch = row.findViewById(R.id.watch);
            viewHolder.buttonEdit = row.findViewById(R.id.edit);
            viewHolder.imageViewMoviePoster = row.findViewById(R.id.appCompatImageView2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (WatchListViewHolder) row.getTag();
        }
        viewHolder.buttonWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(cxt);
                dialog.setContentView(R.layout.watch_list_dialog);
                Button buttonWatched = dialog.findViewById(R.id.buttonWatched);
                Button buttonNotWatching = dialog.findViewById(R.id.buttonNotWatching);
                Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
                if (movie.getWatched()){
                    dialog.setTitle("Remove This Movie?");
                    buttonNotWatching.setText("Remove From History");
                    buttonWatched.setText("Haven't Watched Yet");
                }
                else{
                    dialog.setTitle("Have You Watched This Movie?");
                    buttonNotWatching.setText("Not Watching It");
                    buttonWatched.setText("I've Watched It");
                }
                buttonWatched.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (buttonWatched.getText().toString().toLowerCase().compareToIgnoreCase("i've watched it") == 0){
                            movieDatabase.child(movie.getId() + "").child("watched").setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Toast.makeText(cxt, "Moved To Watch History", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(cxt, "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }else{
                            movieDatabase.child(movie.getId() + "").child("watched").setValue(false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Toast.makeText(cxt, "Moved To Watch List", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(cxt, "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
                buttonNotWatching.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movieDatabase.child(movie.getId() + "").removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        Toast.makeText(cxt, "Movie Removed", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(cxt, "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cxt, Movie_Description.class);
                i.putExtra("id", movie.getId());
                i.putExtra("title", movie.getTitle());
                i.putExtra("overview", movie.getOverview());
                i.putExtra("release_date", movie.getRelease_date());
                i.putExtra("poster_path", movie.getPoster_path());
                i.putExtra("watched", movie.getWatched());
                i.putExtra("notes", movie.getNotes());
                cxt.startActivity(i);
            }
        });
        viewHolder.textViewTitle.setText(movie.getTitle());
        viewHolder.textViewDate.setText(movie.getRelease_date());

        //Assigning Movie Poster
        if (movie.getPoster_path() != null) {
            // show The Image in a ImageView
            new PosterDownloader(viewHolder.imageViewMoviePoster)
                    .execute("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());

        }
        return row;
    }

    public void sortByDate(){
        movieList.sort(Comparator.comparing(Movie::getRelease_date));
    }

    public void sortByDateReverse(){
        movieList.sort(Comparator.comparing(Movie::getRelease_date).reversed());
    }

    public void sortByTitle(){
        movieList.sort(Comparator.comparing(Movie::getTitle));
    }

    public void sortByTitleReverse(){
        movieList.sort(Comparator.comparing(Movie::getTitle).reversed());
    }

    public void clearList(){
        movieList = new ArrayList<Movie>();
    }
}

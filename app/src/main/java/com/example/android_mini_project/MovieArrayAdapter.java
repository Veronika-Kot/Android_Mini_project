package com.example.android_mini_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.android_mini_project.models.Movie;

import java.io.InputStream;
import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private final Context context;
    private final List<Movie> movies;

    public MovieArrayAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, R.layout.movie_rowlayout, objects);

        this.context = context;
        this.movies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Pushing row to the list view
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.movie_rowlayout, parent, false);

        //Assigning row UI elements to appropriate references
        TextView textViewTitle = (TextView) rowView.findViewById(R.id.title);
        TextView textViewRating = (TextView) rowView.findViewById(R.id.rating);
        TextView textViewData = (TextView) rowView.findViewById(R.id.data);

        //Assigning Movies data to UI elements
        textViewTitle.setText(movies.get(position).getTitle());
        textViewRating.setText(movies.get(position).getRelease_date());
        textViewData.setText(movies.get(position).getOverview());

        //Assigning Movie Poster
        if(movies.get(position).getPoster_path() != null) {
            // show The Image in a ImageView
            new DownloadImageTask((ImageView) rowView.findViewById(R.id.appCompatImageView))
                    .execute("http://image.tmdb.org/t/p/w185/"+movies.get(position).getPoster_path());

        }

        return rowView;
    }

    /**
     * ASYNC task to load movie poster
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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


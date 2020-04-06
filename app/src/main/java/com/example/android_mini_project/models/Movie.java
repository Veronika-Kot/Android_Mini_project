package com.example.android_mini_project.models;

import java.io.Serializable;

public class Movie implements Serializable {

    int id;
    String title;
    String release_date;
    String overview;
    String poster_path;
    String notes;
    Boolean watched;

    public Movie(){

    }

    public Movie(int id, String title, String release_date, String overview, String poster_path) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
        this.notes = "";
        this.watched = false;
    }

    public Movie(int id, String title, String release_date, String overview, String poster_path, String notes, Boolean watched) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
        this.notes = notes;
        this.watched = watched;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }
}

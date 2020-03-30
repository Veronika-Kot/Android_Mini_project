package com.example.android_mini_project.models;

import java.io.Serializable;
import java.util.List;

public class MovieResults implements Serializable {
    public List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}

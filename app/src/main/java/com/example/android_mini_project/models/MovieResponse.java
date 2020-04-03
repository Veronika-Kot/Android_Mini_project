package com.example.android_mini_project.models;

import java.io.Serializable;
import java.util.List;

public class MovieResponse implements Serializable {
    public List<Movie> results;
    private Long page;
    private Long total_results;
    private Long total_pages;

    public MovieResponse(List<Movie> results, Long page, Long total_results, Long total_pages) {
        this.results = results;
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Long total_results) {
        this.total_results = total_results;
    }

    public Long getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Long total_pages) {
        this.total_pages = total_pages;
    }
}

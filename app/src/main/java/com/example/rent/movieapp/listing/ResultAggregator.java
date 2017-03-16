package com.example.rent.movieapp.listing;

import com.example.rent.movieapp.detail.MovieItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RENT on 2017-03-15.
 */

public class ResultAggregator {
    private List<MovieListingItem> movieItems = new ArrayList<>();
    private int totalItemsResult;
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<MovieListingItem> getMovieItems() {
        return movieItems;
    }

    public int getTotalItemsResult() {
        return totalItemsResult;
    }

    public void setTotalItemsResult(int totalItemsResult) {
        this.totalItemsResult = totalItemsResult;
    }

    public void addNewItems(List<MovieListingItem> newItems) {
        movieItems.addAll(newItems);
    }
}

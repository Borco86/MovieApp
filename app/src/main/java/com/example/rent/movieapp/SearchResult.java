package com.example.rent.movieapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RENT on 2017-03-07.
 */

public class SearchResult {
    @SerializedName("Search")
    private List<MovieListingItem> items ;
    private String totalResult;
    @SerializedName("Response")
    private String response;

    public List<MovieListingItem> getItems() {
        return items;
    }

    public String getTotalResult() {
        return totalResult;
    }

    public String getResponse() {
        return response;
    }
}

package com.example.rent.movieapp.dagger;

import com.example.rent.movieapp.detail.DetailActivity;
import com.example.rent.movieapp.detail.DetailService;
import com.example.rent.movieapp.listing.ListingActivity;
import com.example.rent.movieapp.search.SearchActivity;

import dagger.Component;

/**
 * Created by RENT on 2017-04-04.
 */
@Component(modules = NetworkModule.class)
public interface AppComponent {

    void inject(ListingActivity listingActivity);

    void inject(DetailActivity detailActivity);

    void inject(SearchActivity searchActivity);
}

package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {

    private static final String SEARCH_TITLE = "searchTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        String title = getIntent().getStringExtra(SEARCH_TITLE);
        getPresenter().getDataAsync(title);
    }

    public static Intent createIntent(Context context, String title) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }


    public void setDataOnUiThread(SearchResult result) {
        runOnUiThread(() -> {
            Stream.of(result.getItems()).forEach(movieListingItem -> Log.d("result", "id " + movieListingItem.getImdbID()));
        });
    }
}

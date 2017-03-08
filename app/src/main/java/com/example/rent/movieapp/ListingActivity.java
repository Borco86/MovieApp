package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
//import rx.schedulers.Schedulers;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {

    private static final String SEARCH_TITLE = "searchTitle";
    private MoviesListAdapter adapter;
    private ViewFlipper viewFlipper;
    private ImageView noInternetImage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        String title = getIntent().getStringExtra(SEARCH_TITLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MoviesListAdapter();
        recyclerView.setAdapter(adapter);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        noInternetImage = (ImageView) findViewById(R.id.no_internet_view);

        getPresenter().getDataAsync(title)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::success, this::error);


    }

    private void error(Throwable throwable) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    private void success(SearchResult searchResult) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        adapter.setItems(searchResult.getItems());
    }

    public static Intent createIntent(Context context, String title) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }


//    public void setDataOnUiThread(SearchResult result, boolean isProblemWithInternetConnection) {
//        runOnUiThread(() -> {
//            if (isProblemWithInternetConnection) {
//                error();
//            } else {
//                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
//                adapter.setItems(result.getItems());
//            }
//        });
//    }
}

package com.example.rent.movieapp.listing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetrofitProvider;
import com.example.rent.movieapp.detail.DetailActivity;
import com.example.rent.movieapp.search.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
//import rx.schedulers.Schedulers;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> implements CurrentItemListener, ShowOnHideCounter, OnMovieItemClickListener {

    private static final String SEARCH_TITLE = "searchTitle";
    private static final String SEARCH_YEAR = "searchYear";
    private static final String SEARCH_TYPE = "searchType";
    public static final int NO_YEAR_SELECTED = -1;
    private MoviesListAdapter adapter;

    //butter knife zastepuje reczne inicjowanie(binduje)
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.no_internet_view)
    ImageView noInternetImage;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_results)
    FrameLayout noResults;
    private EndlessScrollListener endlessScrollListener;

    @BindView(R.id.counter)
    TextView counter;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
            getPresenter().setRetrofit(retrofitProvider.provideRetrofit());
        }

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);

        adapter = new MoviesListAdapter();
        adapter.setOnMovieItemClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager, getPresenter());
        recyclerView.addOnScrollListener(endlessScrollListener);
        endlessScrollListener.setCurrentItemListener(this);
        endlessScrollListener.setShowOrHideCounter(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(title, year, type);
            }
        });

        startLoading(title, year, type);

    }

    private void startLoading(String title, int year, String type) {
        //typowa rx java z nucleusem
        getPresenter().getDataAsync(title, year, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);
    }

    //Butterknife przed adnotacje
    @OnClick(R.id.no_internet_view)
    public void onNoInternetImageView(View view) {
        Toast.makeText(this, "kliknąłeś no internet image view", Toast.LENGTH_SHORT).show();
    }

    private void error(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    public void appendItems(SearchResult searchResult) {
        adapter.addItems(searchResult.getItems());
        endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
    }

    private void success(SearchResult searchResult) {
        swipeRefreshLayout.setRefreshing(false);
        if ("false".equalsIgnoreCase(searchResult.getResponse())) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResults));
        } else {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(swipeRefreshLayout));
            adapter.setItems(searchResult.getItems());
            endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
        }
    }

    public static Intent createIntent(Context context, String title, int year, String type) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TYPE, type);
        return intent;
    }

    @Override
    public void onNewCurrentItem(int currentItem, int totalItemsCount) {
        counter.setText(currentItem + "/" + totalItemsCount);
    }

    @Override
    public void showCounter() {
        counter.setVisibility(View.VISIBLE);
        counter.animate().translationX(0).start();
    }

    @Override
    public void hideCounter() {
        counter.animate().translationX(counter.getWidth() * 2).start();
    }

    @Override
    public void onMovieItemClick(String imdbID) {
        startActivity(DetailActivity.createIntent(this, imdbID));

    }

}

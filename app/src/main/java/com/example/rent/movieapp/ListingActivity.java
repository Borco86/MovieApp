package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
//import rx.schedulers.Schedulers;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {

    private static final String SEARCH_TITLE = "searchTitle";
    private static final String SEARCH_YEAR = "searchYear";
    private static final String SEARCH_TYPE = "searchType";
    private MoviesListAdapter adapter;
    public static final int NO_YEAR_SELECTED = -1;

    //butter knife zastepuje reczne inicjowanie(binduje)
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.no_internet_view)
    ImageView noInternetImage;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_results)
    FrameLayout noResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        ButterKnife.bind(this);

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);
        adapter = new MoviesListAdapter();
        recyclerView.setAdapter(adapter);

        //typowa rx java z nucleusem
        getPresenter().getDataAsync(title,year,type)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::success, this::error);


    }

    //Butterknife przed adnotacje
    @OnClick(R.id.no_internet_view)
    public void onNoInternetImageView(View view){
        Toast.makeText(this, "kliknąłeś no internet image view", Toast.LENGTH_SHORT).show();
    }

    private void error(Throwable throwable) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    private void success(SearchResult searchResult) {
        if("false".equalsIgnoreCase(searchResult.getResponse())){
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        }else{
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        adapter.setItems(searchResult.getItems());
    }}

    public static Intent createIntent(Context context, String title, int year, String type) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TYPE, type);
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

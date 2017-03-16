package com.example.rent.movieapp.listing;

import com.example.rent.movieapp.search.SearchService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> implements OnLoadNextPageListener {

    private ResultAggregator resultAggregator = new ResultAggregator();
    private Retrofit retrofit;
    private String title;
    private String stringYear;
    private String type;
    private boolean isLoadingFromStart;

    public void startLoadingItems(String title, int year, String type) {
        this.title = title;
        this.type = type;
        stringYear = year == ListingActivity.NO_YEAR_SELECTED ? null : String.valueOf(year);

        if (resultAggregator.getMovieItems().size() == 0) {
            loadNextPage(1);
            isLoadingFromStart = true;
        }


    }

    @Override
    protected void onTakeView(ListingActivity listingActivity) {
        super.onTakeView(listingActivity);
        if(!isLoadingFromStart){
            listingActivity.setNewAggregatorResult(resultAggregator);
        }
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }


    @Override
    public void loadNextPage(int page) {
        isLoadingFromStart = false;
        retrofit.create(SearchService.class).search(page, title,
                stringYear, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResult -> {
                    resultAggregator.addNewItems(searchResult.getItems());
                    resultAggregator.setTotalItemsResult(Integer.parseInt(searchResult.getTotalResults()));
                    resultAggregator.setResponse(searchResult.getResponse());
                    getView().setNewAggregatorResult(resultAggregator);
                }, throwable -> {
                    // nop
                });
    }


}

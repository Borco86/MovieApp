package com.example.rent.movieapp.listing;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by RENT on 2017-03-11.
 */

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private static final double PAGE_SIZE = 10;
    private int totalItemsNumber;
    private boolean isLoading;
    private OnLoadNextPageListener listener;
    private ShowOnHideCounter showOrHideCounter;
    private boolean isCounterShown;
    private CurrentItemListener currentItemListener;

    public EndlessScrollListener(LinearLayoutManager layoutManager, OnLoadNextPageListener listener) {
        this.layoutManager = layoutManager;
        this.listener = listener;
    }

    public void setShowOrHideCounter(ShowOnHideCounter showOrHideCounter) {
        this.showOrHideCounter = showOrHideCounter;
    }

    public void setCurrentItemListener(CurrentItemListener currentItemListener) {
        this.currentItemListener = currentItemListener;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int alreadyLoadedItems = layoutManager.getItemCount();
        int currentPage = (int) Math.ceil(alreadyLoadedItems / PAGE_SIZE);
        double numberOfAllPages = Math.ceil(totalItemsNumber / PAGE_SIZE);
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() + 1;

        if (currentPage < numberOfAllPages && lastVisibleItemPosition == alreadyLoadedItems && !isLoading) {
            loadNextPage(++currentPage);
            isLoading = true;
        }
        if (currentItemListener != null) {
            currentItemListener.onNewCurrentItem(lastVisibleItemPosition, totalItemsNumber);
        }


        Log.d("result", "lastVisibleItemPosition" + lastVisibleItemPosition);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (showOrHideCounter != null) {
            if (isCounterShown && newState == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView.postDelayed(() -> {
                    showOrHideCounter.hideCounter();
                    isCounterShown = false;
                }, 3000);


            } else if (!isCounterShown && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                showOrHideCounter.showCounter();
                isCounterShown = true;
            }
        }
    }

    private void loadNextPage(int pageNumber) {
        listener.loadNextPage(pageNumber);
    }

    public void setTotalItemsNumber(int totalItemsNumber) {
        this.totalItemsNumber = totalItemsNumber;
        isLoading = false;
    }
}

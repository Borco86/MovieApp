package com.example.rent.movieapp.listing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by RENT on 2017-03-08.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MyViewHolder> {

    private List<MovieListingItem> items = Collections.emptyList();
    private OnMovieItemClickListener onMovieItemClickListener;
    OnLikeButtonClickListener onLikeButtonClickListener;

    public void setOnLikeButtonClickListener(OnLikeButtonClickListener onLikeButtonClickListener) {
        this.onLikeButtonClickListener = onLikeButtonClickListener;
    }

    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieListingItem movieListingItem = items.get(position);
        Glide.with(holder.poster.getContext()).load(movieListingItem.getPoster()).into(holder.poster);
        holder.titleAndYear.setText(movieListingItem.getTitle() + " (" + movieListingItem.getYear() + ")");
        holder.type.setText("typ: " + movieListingItem.getType());
        holder.itemView.setOnClickListener(v -> {
            if (onMovieItemClickListener != null) {
                onMovieItemClickListener.onMovieItemClick(movieListingItem.getImdbID());
            }
        });

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (onLikeButtonClickListener != null) {
                    onLikeButtonClickListener.onLikeButtonClick(movieListingItem);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<MovieListingItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView poster;
        TextView titleAndYear;
        TextView type;
        LikeButton likeButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            poster = (ImageView) itemView.findViewById(R.id.poster);
            titleAndYear = (TextView) itemView.findViewById(R.id.title_and_year);
            type = (TextView) itemView.findViewById(R.id.type);
            likeButton = (LikeButton) itemView.findViewById(R.id.star_button);
        }
    }
}


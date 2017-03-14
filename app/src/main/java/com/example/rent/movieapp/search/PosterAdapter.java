package com.example.rent.movieapp.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.listing.OnMovieItemClickListener;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by RENT on 2017-03-11.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {

    private List<SimpleMovieItem> simpleMovieItem = Collections.emptyList();
    private OnMovieItemClickListener onMovieItemClickListener;

    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.posterImageView.getContext()).load(simpleMovieItem.get(position).getPoster()).into(holder.posterImageView);

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMovieItemClickListener!=null){
                   onMovieItemClickListener.onMovieItemClick(simpleMovieItem.get(position).getImdbID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return simpleMovieItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = ButterKnife.findById(itemView, R.id.poster);
        }
    }

    public void setSimpleMovieItem(List<SimpleMovieItem> simpleMovieItem) {
        this.simpleMovieItem = simpleMovieItem;
        notifyDataSetChanged();
    }
}

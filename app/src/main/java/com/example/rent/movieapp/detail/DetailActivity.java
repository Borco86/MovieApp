package com.example.rent.movieapp.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetrofitProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(DetailPresenter.class)
public class DetailActivity extends NucleusAppCompatActivity<DetailPresenter> {

    private static final String ID_KEY = "id_key";
    private Disposable subscribe;

    @BindView(R.id.poster)
    ImageView poster;

    @BindView(R.id.title_and_year)
    TextView titleAndYear;
    @BindView(R.id.type)
    TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String imdbId = getIntent().getStringExtra(ID_KEY);

        RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
        getPresenter().setRetrofit(retrofitProvider.provideRetrofit());

        subscribe = getPresenter().loadDetail(imdbId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);
    }

    // zabezpieczenie przeciwko wyciekom pamięci
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            subscribe.dispose();
        }
    }

    private void success(MovieItem movieItem) {
        Glide.with(this).load(movieItem.getPoster()).into(poster);
        titleAndYear.setText(movieItem.getTitle()+" ("+movieItem.getYear()+")");
        type.setText(movieItem.getType());


    }

    private void error(Throwable throwable) {
    }

    public static Intent createIntent(Context context, String imdbID) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ID_KEY, imdbID);
        return intent;
    }
}

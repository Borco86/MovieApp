package com.example.rent.movieapp.search;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.rent.movieapp.MovieAppApplication;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.detail.DetailActivity;
import com.example.rent.movieapp.listing.ListingActivity;
import com.example.rent.movieapp.listing.MovieListingItem;
import com.example.rent.movieapp.listing.OnMovieItemClickListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements OnMovieItemClickListener {
    private static final String NUMBER_PICKER_STATE = "number_picker_state";

    private Map<Integer, String> apiKeysMap = new HashMap<Integer, String>() {{
        put(R.id.radio_movies, "movie");
        put(R.id.radio_series, "series");
        put(R.id.radio_games, "game");
        put(R.id.radio_episodes, "episode");
    }};
    @BindView(R.id.number_picker)
    NumberPicker numberPicker;
    @BindView(R.id.search_edit_text)
    TextInputEditText editText;
    @BindView(R.id.search_button)
    ImageButton searchButton;
    @BindView(R.id.year_checkbox)
    CheckBox yearCheckBox;
    @BindView(R.id.type_checkbox)
    CheckBox typeCheckBox;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.poster_recycler_view)
    RecyclerView posterRecyclerView;
    private PosterAdapter posterAdapter;

    @Inject
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(year);
        numberPicker.setValue(year);
        numberPicker.setWrapSelectorWheel(true);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_NULL && event.getAction()==KeyEvent.ACTION_DOWN) {
                Log.d("result", "action" + event.getAction());
                onSearchButtonClick();
            }
            return false;
        });

        posterAdapter = new PosterAdapter();
        //posterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        posterRecyclerView.setLayoutManager(layoutManager);
        posterRecyclerView.setHasFixedSize(true);
        posterRecyclerView.setAdapter(posterAdapter);
        posterRecyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        MovieAppApplication movieAppApplication = (MovieAppApplication) getApplication();
        movieAppApplication.getAppComponent().inject(this);

        SearchService searchService = retrofit.create(SearchService.class);
        searchService.search(1,"a+", "2016", null)
                .flatMap(searchResult -> Observable.fromIterable(searchResult.getItems()))
                .map((Function<MovieListingItem, SimpleMovieItem>) movieListingItem -> new SimpleMovieItem(movieListingItem.getPoster(),movieListingItem.getImdbID()))
                .filter(simpleMovieItem -> !"N/A".equalsIgnoreCase(simpleMovieItem.getPoster()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(this::success, this::error);
        posterAdapter.setOnMovieItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUMBER_PICKER_STATE, numberPicker.getValue());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberPicker.setValue(savedInstanceState.getInt(NUMBER_PICKER_STATE));
    }

    private void success(List<SimpleMovieItem> list) {
        posterAdapter.setSimpleMovieItem(list);
    }

    private void error(Throwable throwable) {

    }


    @OnCheckedChanged(R.id.type_checkbox)
    void onTypeCheckboxStateChange(CompoundButton buttonView, boolean isChecked) {
        radioGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.year_checkbox)
    void onCheckboxStateChanged(CompoundButton buttonView, boolean isChecked) {
        numberPicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.search_button)
    void onSearchButtonClick() {
        int checkRadioId = radioGroup.getCheckedRadioButtonId();
        String typeKey = typeCheckBox.isChecked() ? apiKeysMap.get(checkRadioId) : null;
        int year = yearCheckBox.isChecked() ? numberPicker.getValue() : ListingActivity.NO_YEAR_SELECTED;
        startActivity(ListingActivity.createIntent(SearchActivity.this, editText.getText().toString(), year, typeKey));
    }


    @Override
    public void onMovieItemClick(String imdbID) {
        startActivity(DetailActivity.createIntent(this, imdbID));
    }
}

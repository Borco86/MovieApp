package com.example.rent.movieapp;

import io.reactivex.Observable;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> {

    private Retrofit retrofit;

    // konfiguracja retrofita
    public ListingPresenter() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.omdbapi.com/")
                .build();
    }

    public Observable<SearchResult> getDataAsync(String title, int year, String type) {
       String stringYear = year == ListingActivity.NO_YEAR_SELECTED ? null : String.valueOf(year);
        return retrofit.create(SearchService.class).search(title, stringYear, type);

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    String result = getData(title);
//                    SearchResult searchResult = new Gson().fromJson(result, SearchResult.class);
//                    getView().setDataOnUiThread(searchResult, false);
//                } catch (IOException e) {
//                    getView().setDataOnUiThread(null, true);
//                }
//            }
//        }.start();
    }


//    public String getData(String title) throws IOException {
//        String stringUrl = "https://www.omdbapi.com/?s=" + title;
//        URL url = new URL(stringUrl);
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setConnectTimeout(3000);
//        InputStream inputStream = urlConnection.getInputStream();
//        return convertStreamToString(inputStream);
//    }
//
//    private String convertStreamToString(InputStream inputStream) {
//        Scanner scanner = new Scanner(inputStream).useDelimiter("//A");
//        return scanner.hasNext() ? scanner.next() : "";
//    }

}

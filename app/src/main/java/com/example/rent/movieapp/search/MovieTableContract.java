package com.example.rent.movieapp.search;

import android.provider.BaseColumns;

/**
 * Created by RENT on 2017-04-01.
 */

public class MovieTableContract implements BaseColumns {
    public static final String TABLE_NAME = "MOVIES";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_YEAR = "YEAR";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_POSTER = "POSTER";
}

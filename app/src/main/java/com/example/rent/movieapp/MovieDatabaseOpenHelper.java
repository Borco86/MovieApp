package com.example.rent.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rent.movieapp.search.MovieTableContract;

/**
 * Created by RENT on 2017-04-01.
 */

public class MovieDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "my_movies.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
            + MovieTableContract.TABLE_NAME + " ("
            + MovieTableContract.COLUMN_TITLE + " TEXT, "
            + MovieTableContract.COLUMN_TYPE + " TEXT, "
            + MovieTableContract.COLUMN_YEAR + " TEXT, "
            + MovieTableContract.COLUMN_POSTER + " TEXT )";
    private static String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + MovieTableContract.TABLE_NAME;

    public MovieDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        }
    }
}

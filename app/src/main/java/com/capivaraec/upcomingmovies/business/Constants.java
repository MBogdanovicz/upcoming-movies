package com.capivaraec.upcomingmovies.business;

/**
 * Created by marcelo on 18/04/17.
 */

public class Constants {

    public static final long CACHE_TTL = 1000 * 60 * 60 * 24 * 5; //5 days
    public static final String CACHE_KEY = "GENRES_CACHE_KEY";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_BUNDLE_KEY = "MOVIE_BUNDLE_KEY";
}

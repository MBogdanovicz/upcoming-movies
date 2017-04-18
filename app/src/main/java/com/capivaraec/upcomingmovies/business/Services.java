package com.capivaraec.upcomingmovies.business;

import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;
import com.capivaraec.upcomingmovies.restAPI.RestAPI;

import java.util.Locale;

import io.reactivex.Observable;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class Services {

    public static Observable<Upcoming> loadMovies(int page) {

        return RestAPI.loadMovies(page, getLanguage());
    }

    public static Observable<Result> loadMovieDetails(int movieId) {
        return RestAPI.loadMovieDetails(movieId, getLanguage());
    }

    private static String getLanguage() {
        return Locale.getDefault().toString().replace("_", "-");
    }
}

package com.capivaraec.upcomingmovies.restAPI;

import com.capivaraec.upcomingmovies.object.Movie;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class RestAPI {

    public static Observable<ArrayList<Movie>> loadMovies(int page) {
        return new Observable<ArrayList<Movie>>() {
            @Override
            protected void subscribeActual(Observer<? super ArrayList<Movie>> observer) {

            }
        };
    }
}

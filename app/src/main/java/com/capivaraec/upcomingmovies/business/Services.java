package com.capivaraec.upcomingmovies.business;

import com.capivaraec.upcomingmovies.object.Movie;
import com.capivaraec.upcomingmovies.restAPI.RestAPI;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class Services {

    public static Observable<ArrayList<Movie>> loadMovies(int page) {
        return RestAPI.loadMovies(page);
    }
}

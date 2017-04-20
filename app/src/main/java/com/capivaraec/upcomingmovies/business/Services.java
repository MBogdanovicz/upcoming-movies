package com.capivaraec.upcomingmovies.business;

import android.content.Context;

import com.capivaraec.upcomingmovies.model.Genres;
import com.capivaraec.upcomingmovies.model.Result;
import com.capivaraec.upcomingmovies.model.Upcoming;
import com.capivaraec.upcomingmovies.restAPI.RestAPI;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class Services {

    public static Observable<Upcoming> loadMovies(int page) {
        return RestAPI.loadMovies(page, Utils.getLanguage());
    }

    public static Observable<Result> loadMovieDetails(int movieId) {
        return RestAPI.loadMovieDetails(movieId, Utils.getLanguage());
    }

    public static Observable<Genres> getAllGenres(final Context context) {

        try {
            Genres genres = Utils.getGenresCache(context);

            if (genres != null) {
                return Observable.just(genres);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestAPI.getAllGenres(Utils.getLanguage()).map(new Function<Genres, Genres>() {
            @Override
            public Genres apply(Genres genres) throws Exception {
                Utils.setGenresCache(context, genres);

                return genres;
            }
        });
    }

    public static Observable<Upcoming> searchMovies(int page, String query) {
        return RestAPI.searchMovies(page, query, Utils.getLanguage());
    }
}

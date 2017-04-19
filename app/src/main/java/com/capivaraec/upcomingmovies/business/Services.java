package com.capivaraec.upcomingmovies.business;

import android.content.Context;
import android.provider.Settings;

import com.capivaraec.upcomingmovies.object.Genres;
import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;
import com.capivaraec.upcomingmovies.restAPI.RestAPI;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ExceptionHelper;

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

    public static Observable<Void> getAllGenres(final Context context) {

        try {
            Genres genres = Utils.getGenresCache(context);

            if (genres != null) {
                return Observable.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestAPI.getAllGenres(Utils.getLanguage()).map(new Function<Genres, Void>() {
            @Override
            public Void apply(Genres genres) throws Exception {
                Utils.setGenresCache(context, genres);

                return null;
            }
        });
    }
}

package com.capivaraec.upcomingmovies.business;

import android.content.Context;
import android.provider.Settings;

import com.capivaraec.upcomingmovies.object.Genres;
import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;
import com.capivaraec.upcomingmovies.restAPI.RestAPI;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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

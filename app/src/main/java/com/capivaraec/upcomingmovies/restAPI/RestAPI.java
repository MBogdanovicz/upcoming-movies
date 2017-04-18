package com.capivaraec.upcomingmovies.restAPI;

import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class RestAPI {

    private static final String API_KEY = "1f54bd990f1cdfb230adb312546d765d";

    public static Observable<Upcoming> loadMovies(int page, String language) {

        Map<String, String> params = getMoviesListParams(page, language);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/upcoming")
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Upcoming.class);
    }

    private static Map<String, String> getMoviesListParams(int page, String language) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("language", language);
        params.put("page", String.valueOf(page));

        return params;
    }

    public static Observable<Result> loadMovieDetails(int movieId, String language) {

        Map<String, String> params = getMovieDetailsParams(language);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/{movieId}")
                .addPathParameter("movieId", String.valueOf(movieId))
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Result.class);
    }

    private static Map<String, String> getMovieDetailsParams(String language) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("language", language);

        return params;
    }
}

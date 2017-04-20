package com.capivaraec.upcomingmovies.restAPI;

import com.capivaraec.upcomingmovies.model.Genres;
import com.capivaraec.upcomingmovies.model.Result;
import com.capivaraec.upcomingmovies.model.Upcoming;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class RestAPI {

    private static final String API_KEY = "1f54bd990f1cdfb230adb312546d765d";

    private static Map<String, String> getBasicParams(String language) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("language", language);

        return params;
    }

    private static Map<String, String> getMoviesListParams(int page, String language) {
        Map<String, String> params = getBasicParams(language);
        params.put("page", String.valueOf(page));

        return params;
    }

    public static Observable<Upcoming> loadMovies(int page, String language) {

        Map<String, String> params = getMoviesListParams(page, language);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/upcoming")
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Upcoming.class);
    }

    public static Observable<Result> loadMovieDetails(int movieId, String language) {

        Map<String, String> params = getBasicParams(language);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/{movieId}")
                .addPathParameter("movieId", String.valueOf(movieId))
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Result.class);
    }

    public static Observable<Genres> getAllGenres(String language) {
        Map<String, String> params = getBasicParams(language);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/genre/movie/list")
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Genres.class);
    }

    public static Observable<Upcoming> searchMovies(int page, String query, String language) {

        try {
            Map<String, String> params = getMoviesListParams(page, language);
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            params.put("query", encodedQuery);

            return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/search/movie")
                    .addQueryParameter(params)
                    .build()
                    .getObjectObservable(Upcoming.class);
        } catch (Exception e) {
            e.printStackTrace();

            return Observable.error(e);
        }
    }
}

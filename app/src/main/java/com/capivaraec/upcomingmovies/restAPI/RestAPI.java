package com.capivaraec.upcomingmovies.restAPI;

import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by marcelobogdanovicz on 17/04/17.
 */

public class RestAPI {

    private static final String API_KEY = "1f54bd990f1cdfb230adb312546d765d";

    public static Observable<Upcoming> loadMovies(int page) {

        Map<String, String> params = getParams(page);

        return Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/upcoming")
                .addQueryParameter(params)
                .build()
                .getObjectObservable(Upcoming.class);
    }

    private static Map<String, String> getParams(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("language", "en-US");
        params.put("page", String.valueOf(page));

        return params;
    }
}

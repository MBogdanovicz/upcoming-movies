package com.capivaraec.upcomingmovies.business;

import android.content.Context;

import com.capivaraec.upcomingmovies.model.Genre;
import com.capivaraec.upcomingmovies.model.Genres;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcelo on 18/04/17.
 */

public class Utils {

    private static Genres genres;
    public static String formatDate(String date, int style) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            return DateFormat.getDateInstance(style).format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getLanguage() {
        return Locale.getDefault().toString().replace("_", "-");
    }

    public static void setGenresCache(Context context, Genres genres) throws Exception {
        genres.setExpirationDate(System.currentTimeMillis() + Constants.CACHE_TTL);

        InternalStorage.writeObject(context, Constants.CACHE_KEY, genres);
    }

    public static Genres getGenresCache(Context context) {
        try {
            Genres genres = (Genres) InternalStorage.readObject(context, Constants.CACHE_KEY);

            if (genres != null && genres.getExpirationDate() > System.currentTimeMillis()) {
                return genres;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getGenres(Context context, List<Integer> genreIds) {

        List<String> genres = new ArrayList<>();
        List<Genre> all;

        if (Utils.genres == null) {
            Utils.genres = getGenresCache(context);
        }

        all = Utils.genres.getGenres();

        try {

            for (Integer id : genreIds) {
                for (Genre genre : all) {
                    if (genre.equals(id)) {
                        genres.add(genre.getName());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genres;
    }

    public static String getPosterURL(String posterPath) {
        return Constants.POSTER_BASE_URL + posterPath;
    }
}

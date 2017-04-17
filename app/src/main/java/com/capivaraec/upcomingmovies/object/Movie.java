package com.capivaraec.upcomingmovies.object;

/**
 * Created by marcelo on 16/04/17.
 */

public class Movie {

    private String title;
    private String releaseDate;

    public Movie(String title, String date) {
        this.title = title;
        this.releaseDate = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}

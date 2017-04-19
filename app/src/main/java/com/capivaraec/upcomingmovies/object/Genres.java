package com.capivaraec.upcomingmovies.object;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcelo on 18/04/17.
 */

public class Genres implements Serializable {

    private long expirationDate;
    private List<Genre> genres;

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}

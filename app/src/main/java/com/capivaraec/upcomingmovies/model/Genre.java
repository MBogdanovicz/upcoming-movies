package com.capivaraec.upcomingmovies.model;

import java.io.Serializable;

/**
 * Created by marcelobogdanovicz on 18/04/17.
 */

public class Genre implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return id == (Integer) obj;
        }
        return super.equals(obj);
    }
}

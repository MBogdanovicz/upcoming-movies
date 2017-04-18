package com.capivaraec.upcomingmovies.object;

import java.util.List;

/**
 * Created by marcelo on 18/04/17.
 */

public class Upcoming {

    private int page;
    private List<Result> results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}

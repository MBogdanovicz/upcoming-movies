package com.capivaraec.upcomingmovies.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.adapter.MoviesListAdapter;
import com.capivaraec.upcomingmovies.adapter.DividerItemDecoration;
import com.capivaraec.upcomingmovies.business.Constants;
import com.capivaraec.upcomingmovies.business.Services;
import com.capivaraec.upcomingmovies.model.Genres;
import com.capivaraec.upcomingmovies.model.Result;
import com.capivaraec.upcomingmovies.model.Upcoming;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoviesListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private MoviesListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressBar mProgressBar;
    private SearchView searchView;

    private int page = 1, searchingPage = 1;
    private int totalPages, searchingTotalPages;
    private List<Result> movies;
    private List<Result> filteredMovies;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading;
    private boolean searching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        AndroidNetworking.initialize(this);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.movies_list_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                movies = null;
                loadMovies(page);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && ((visibleItemCount + pastVisibleItems) >= totalItemCount)) {
                        loading = true;
                        if (searching) {
                            if (searchingPage < searchingTotalPages) {
                                search(++searchingPage, searchView.getQuery().toString());
                            }
                        } else {
                            if (page < totalPages) {
                                loadMovies(++page);
                            }
                        }
                    }
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateGenres();

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        setSearchListeners(searchView);

        return true;
    }

    private void updateGenres() {
        Services.getAllGenres(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Genres>() {
                    @Override
                    public void accept(Genres genres) throws Exception {
                        loadMovies(1);
                    }
                });
    }

    private void loadMovies(final int page) {
        Services.loadMovies(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Upcoming>() {

            @Override
            public void accept(Upcoming upcoming) throws Exception {
                if (movies == null) {
                    movies = upcoming.getResults();

                    mAdapter = new MoviesListAdapter(MoviesListActivity.this, movies);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    movies.addAll(upcoming.getResults());

                    mAdapter.notifyDataSetChanged();
                }

                totalPages = upcoming.getTotal_pages();

                mProgressBar.setVisibility(View.GONE);
                mSwipeRefresh.setRefreshing(false);
                loading = false;
            }
        });
    }

    public void showMovieDetails(Result movie) {
        Intent intent = new Intent(getBaseContext(), MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_BUNDLE_KEY, movie);
        startActivity(intent);
    }

    private void setSearchListeners(final SearchView searchView) {
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSwipeRefresh.setEnabled(true);
                searchView.onActionViewCollapsed();
                searching = false;
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeRefresh.setEnabled(false);
                searching = true;
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String query) {
        searchingPage = 1;
        if (query.length() > 0) {
            search(searchingPage, query);
        } else {

            mAdapter = new MoviesListAdapter(MoviesListActivity.this, movies);
            mRecyclerView.setAdapter(mAdapter);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchingPage = 1;
        search(searchingPage, query);
        return true;
    }

    private void search(final int page, final String query) {

        Services.searchMovies(page, query).debounce(1300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Upcoming>() {
                    @Override
                    public void accept(Upcoming upcoming) throws Exception {
                        searchingTotalPages = upcoming.getTotal_pages();

                        if (page == 1) {
                            filteredMovies = upcoming.getResults();
                            mAdapter.updateData(filteredMovies);
                        } else {
                            filteredMovies.addAll(upcoming.getResults());
                            mAdapter.notifyDataSetChanged();
                        }
                        loading = false;

                    }
                });
    }
}
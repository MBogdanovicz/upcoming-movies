package com.capivaraec.upcomingmovies.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.adapter.Adapter;
import com.capivaraec.upcomingmovies.adapter.DividerItemDecoration;
import com.capivaraec.upcomingmovies.business.Services;
import com.capivaraec.upcomingmovies.object.Result;
import com.capivaraec.upcomingmovies.object.Upcoming;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoviesListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressBar mProgressBar;

    private int page = 1;
    private int totalPages;
    private List<Result> movies;
    private List<Result> filteredMovies;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading;

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
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && page < totalPages && ((visibleItemCount + pastVisiblesItems) >= totalItemCount)) {
                        loading = true;
                        loadMovies(++page);
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
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        setSearchListeners(searchView);

        return true;
    }

    private void updateGenres() {
        Services.getAllGenres(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
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

                    mAdapter = new Adapter(MoviesListActivity.this, movies);
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
        intent.putExtra("MOVIE", movie);
        startActivity(intent);
    }

    private void setSearchListeners(final SearchView searchView) {
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSwipeRefresh.setEnabled(true);
                searchView.onActionViewCollapsed();
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeRefresh.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String query) {//TODO: colocar aqui lógica de esperar dois segundos
        search(query);
        Log.d("search", "searching: " + query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        Log.d("search", "submited: " + query);
        return false;
    }

    private void search(final String query) {

        filteredMovies = new ArrayList<>();
        String[] queries = query.split(" ");

        for (Result movie : movies) {
            for (String word : queries) {
                if (movie.getTitle().toLowerCase().contains(word.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            }
        }

        mAdapter.updateData(filteredMovies);
    }
}
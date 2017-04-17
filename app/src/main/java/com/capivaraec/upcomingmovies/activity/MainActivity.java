package com.capivaraec.upcomingmovies.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.adapter.Adapter;
import com.capivaraec.upcomingmovies.adapter.DividerItemDecoration;
import com.capivaraec.upcomingmovies.object.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressBar mProgressBar;

    private ArrayList<Movie> movies;
    private ArrayList<Movie> filteredMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.movies_list_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movies.add(0, new Movie("O Exterminador do Futuro 2", "10/02/1986"));
                mAdapter.updateData(movies);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //TODO: carregar lista do servidor
        movies = new ArrayList<>();
        movies.add(new Movie("A maldição da ilha dos macacos", "12/12/2017"));
        movies.add(new Movie("Star Wars", "12/12/2017"));
        movies.add(new Movie("Os Simpsons", "12/12/2017"));
        movies.add(new Movie("Psicose", "12/12/2017"));
        movies.add(new Movie("História sem fim", "12/12/2017"));

        filteredMovies = movies;

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        mAdapter = new Adapter(filteredMovies);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar.setVisibility(View.GONE);
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
        for (Movie movie : movies) {
            for (String word : queries) {
                if (movie.getTitle().toLowerCase().contains(word.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            }
        }

        mAdapter.updateData(filteredMovies);
    }
}
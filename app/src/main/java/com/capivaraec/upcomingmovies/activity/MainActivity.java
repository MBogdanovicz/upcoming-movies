package com.capivaraec.upcomingmovies.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.adapter.Adapter;
import com.capivaraec.upcomingmovies.adapter.DividerItemDecoration;
import com.capivaraec.upcomingmovies.object.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> movies;

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
                mAdapter.notifyDataSetChanged();
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

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        mAdapter = new Adapter(movies);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar.setVisibility(View.GONE);
    }
}

package com.capivaraec.upcomingmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.business.Constants;
import com.capivaraec.upcomingmovies.business.Services;
import com.capivaraec.upcomingmovies.business.Utils;
import com.capivaraec.upcomingmovies.model.Result;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by marcelobogdanovicz on 18/04/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private Result movie;
    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView genre;
    private TextView overview;
    private LinearLayout genresLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        poster = (ImageView) findViewById(R.id.detail_movie_poster);
        title = (TextView) findViewById(R.id.detail_movie_title);
        releaseDate = (TextView) findViewById(R.id.detail_release_date);
        genre = (TextView) findViewById(R.id.detail_genre);
        overview = (TextView) findViewById(R.id.detail_overview);
        genresLayout = (LinearLayout) findViewById(R.id.genres_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            movie = (Result) bundle.getSerializable(Constants.MOVIE_BUNDLE_KEY);
            loadMovieDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieDetails() {
        showDetails();

        Services.loadMovieDetails(movie.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {

                    @Override
                    public void accept(Result movie) throws Exception {
                        MovieDetailActivity.this.movie = movie;
                        showDetails();
                    }
                });
    }

    private void showDetails() {
        Picasso.with(this).load(Utils.getPosterURL(movie.getPoster_path())).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(Utils.formatDate(movie.getRelease_date(), DateFormat.MEDIUM));
        overview.setText(movie.getOverview());
        genre.setText(setMovieGenres());
    }

    private String setMovieGenres() {
        if (movie.getGenres() == null) {
            genresLayout.setVisibility(View.GONE);

            return "";
        } else {
            genresLayout.setVisibility(View.VISIBLE);

            return TextUtils.join(", ", movie.getGenres());
        }
    }
}

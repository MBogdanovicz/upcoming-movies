package com.capivaraec.upcomingmovies.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.business.Services;
import com.capivaraec.upcomingmovies.object.Result;

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
    private LinearLayout secondSeparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        poster = (ImageView) findViewById(R.id.detail_movie_poster);
        title = (TextView) findViewById(R.id.detail_movie_title);
        releaseDate = (TextView) findViewById(R.id.detail_release_date);
        genre = (TextView) findViewById(R.id.detail_genre);
        overview = (TextView) findViewById(R.id.detail_overview);
        secondSeparator = (LinearLayout) findViewById(R.id.second_separator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            movie = (Result) bundle.getSerializable("MOVIE");
            loadMovieDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
        //poster
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getRelease_date());
        overview.setText(movie.getOverview());
        genre.setText(setMovieGenres());
    }

    private String setMovieGenres() {
        if (movie.getGenres() == null) {
            genre.setVisibility(View.GONE);
            secondSeparator.setVisibility(View.GONE);

            return "";
        } else {
            genre.setVisibility(View.VISIBLE);
            secondSeparator.setVisibility(View.VISIBLE);

            return TextUtils.join("\n", movie.getGenres());
        }
    }
}

package com.capivaraec.upcomingmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.activity.MoviesListActivity;
import com.capivaraec.upcomingmovies.business.Utils;
import com.capivaraec.upcomingmovies.object.Result;

import java.util.List;

/**
 * Created by marcelo on 16/04/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Result> mMovies;
    private MoviesListActivity activity;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvGenres;
        private TextView tvReleaseDate;
        private ImageView ivPoster;

        private ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.movie_title);
            tvReleaseDate = (TextView) v.findViewById(R.id.movie_release_date);
            tvGenres = (TextView) v.findViewById(R.id.movie_genres);
            ivPoster = (ImageView) v.findViewById(R.id.movie_poster);
        }
    }

    public Adapter(MoviesListActivity activity, List<Result> movies) {
        mMovies = movies;
        this.activity = activity;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Result movie = mMovies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvReleaseDate.setText(movie.getRelease_date());
        holder.tvGenres.setText(TextUtils.join(", ", Utils.getGenres(activity, movie.getGenre_ids())));
        //TODO: colocar imagem

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showMovieDetails(movie);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void updateData(List<Result> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}

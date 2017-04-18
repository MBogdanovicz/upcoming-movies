package com.capivaraec.upcomingmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.activity.MainActivity;
import com.capivaraec.upcomingmovies.object.Result;

import java.util.List;

/**
 * Created by marcelo on 16/04/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Result> mMovies;
    private MainActivity activity;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvReleaseDate;//TODO: implementar
        private ImageView ivPoster;

        private ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.movie_title);
            tvDate = (TextView) v.findViewById(R.id.movie_release_date);
        }
    }

    public Adapter(MainActivity activity, List<Result> movies) {
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
        holder.tvDate.setText(movie.getRelease_date());

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

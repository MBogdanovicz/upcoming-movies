package com.capivaraec.upcomingmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capivaraec.upcomingmovies.R;
import com.capivaraec.upcomingmovies.object.Movie;

import java.util.ArrayList;

/**
 * Created by marcelo on 16/04/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Movie> mMovies;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.movie_title);
        }
    }

    public Adapter(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Movie movie = mMovies.get(position);
        holder.tvName.setText(movie.getTitle());

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}

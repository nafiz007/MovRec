package cs2340.teamnasamovierecommender.pojo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs2340.teamnasamovierecommender.R;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sai on 4/20/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> implements Filterable {
    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.movie_list, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.movie_list, parent, false);

        Movie movie = getItem(position);
        TextView movieName = (TextView) customView.findViewById(R.id.movieName);
        TextView movieYear = (TextView) customView.findViewById(R.id.movieYear);

        movieName.setText(movie.getName());
        if (movie.getRelease().length() > 3) {
            movieYear.setText(movie.getRelease().substring(0, 4));
        } else {
            movieYear.setText("????");
        }

        return customView;
    }
}
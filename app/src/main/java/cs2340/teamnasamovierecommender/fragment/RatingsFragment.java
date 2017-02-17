package cs2340.teamnasamovierecommender.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.activity.MovieDetailActivity;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.Movie;
import cs2340.teamnasamovierecommender.pojo.MovieAdapter;

/**
 * This class handles the Ratings fragment. The user gets here by tapping on the ratings tab on the
 * home screen.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class RatingsFragment extends Fragment {

    static DBHandler handler;
    SharedPreferences sharedPreferences;
    String username;
    ArrayList<Movie> movieArrayList;
    ListAdapter movieAdapter;
    ListView ratedList;

    public static ArrayList<Movie> getRatedMovies() {
        Cursor c = handler.rawQuery("select * from movies");

        ArrayList<Movie> ratedMovies = new ArrayList<Movie>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {
                String movieName = c.getString(c.getColumnIndex(DBHandler.COLUMN_MOVIE_NAME));
                String movieId = c.getString(c.getColumnIndex(DBHandler.COLUMN_MOVIE_ID));
                String movieYear = c.getString(c.getColumnIndex(DBHandler.COLUMN_MOVIE_YEAR));
                Movie movie = new Movie(movieName, Integer.parseInt(movieId), movieYear);
                ratedMovies.add(movie);
                c.moveToNext();
            }
        }

        Collections.reverse(ratedMovies);

        return ratedMovies;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           LayoutInflater: The LayoutInflater object that can be used to
     *                           inflate any views in the fragment.
     * @param container          ViewGroup: If non-null, this is the parent view that the fragment's
     *                           UI should be attached to. The fragment should not add the view
     *                           itself, but this can be used to generate the LayoutParams of the
     *                           view.
     * @param savedInstanceState Bundle: If non-null, this fragment is being re-constructed from a
     *                           previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString("Username", "userDefault");
        handler = new DBHandler(getContext(), username.toUpperCase() + ".db");

        View v = inflater.inflate(R.layout.fragment_ratings, container, false);

        ratedList = (ListView) v.findViewById(R.id.ratingsList);

        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.overflow_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onResume() {
        super.onResume();

        movieArrayList = getRatedMovies();
        movieAdapter = new MovieAdapter(getContext(), movieArrayList);

        ratedList.setAdapter(movieAdapter);

        ratedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieArrayList.get(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        Log.d("ratingsFragment", "onResume");
    }

}

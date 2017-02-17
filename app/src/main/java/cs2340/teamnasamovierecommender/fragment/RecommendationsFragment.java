package cs2340.teamnasamovierecommender.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.activity.MovieDetailActivity;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.JsonTask;
import cs2340.teamnasamovierecommender.pojo.Movie;
import cs2340.teamnasamovierecommender.pojo.MovieAdapter;

/**
 * This class handles the Recommendations fragment. The user gets here by tapping on the
 * recommendations tab on the home screen.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class RecommendationsFragment extends Fragment {

    static DBHandler handler;
    ArrayList<Movie> movieArrayList;
    ArrayList<Movie> recommendationsArrayList;
    ListAdapter movieAdapter;
    String username;
    SharedPreferences sharedPreferences;
    private ListView recommendations;
    private ProgressBar progressBar;
    private JsonTask task;
    private JSONObject result;

    public static ArrayList<Movie> getRatedMovies() {
        Cursor c = handler.rawQuery("select * from movies order by rating desc");

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        recommendations = (ListView) view.findViewById(R.id.recommendationsList);

        movieArrayList = new ArrayList<Movie>();
        recommendationsArrayList = new ArrayList<Movie>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString("Username", "userDefault");
        handler = new DBHandler(getContext(), username.toUpperCase() + ".db");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        movieArrayList = getRatedMovies();

        task = new JsonTask(this);
        try {
            String baseURL = "http://api.themoviedb.org/3/movie/" + movieArrayList.get(0).getId() + "/similar";
            final String QUESTION = "?";
            final String API_KEY = "api_key=9910680035e7aaa74e54beea1da21ceb";
            String queryString = baseURL + QUESTION + API_KEY;
            Log.d("Query", queryString);

            task.execute(queryString);

            movieAdapter = new MovieAdapter(getContext(), recommendationsArrayList);

            recommendations.setAdapter(movieAdapter);

            recommendations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = recommendationsArrayList.get(position);
                    Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        } catch (IndexOutOfBoundsException e) {
            Log.d("No recs", "No recommendations to show.");
        }

        Log.d("recommendationsFragment", "onResume");
    }

    public void showProgressBar() {
        recommendations.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    public void hideProgressBar() {
        recommendations.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void populateList(JSONObject result) {
        try {
            JSONArray jsArrayResults = result.getJSONArray("results");
            for (int i = 0; i < jsArrayResults.length(); i++) {
                String movieName = jsArrayResults.getJSONObject(i).getString("original_title");
                String movieYear = jsArrayResults.getJSONObject(i).getString("release_date");
                int movieId = Integer.parseInt(jsArrayResults.getJSONObject(i).getString("id"));
                String backdropPath = jsArrayResults.getJSONObject(i).getString("backdrop_path");
                Movie movie = new Movie(movieName, movieId, movieYear);
                movie.setBackdrop(backdropPath);
                recommendationsArrayList.add(movie);
            }
        } catch (JSONException e) {
            Log.d("JsonError", e.toString());
        }
    }

    public void onActivityCreated(Bundle savedStateInstance) {
        if (isTaskRunning(task)) {
            showProgressBar();
        } else {
            hideProgressBar();
        }
        if (result != null) {
            populateList(result);
        }

        super.onActivityCreated(savedStateInstance);
    }

    public boolean isTaskRunning(AsyncTask task) {
        if (task == null) {
            return false;
        } else if (task.getStatus() == AsyncTask.Status.FINISHED) {
            return false;
        } else {
            return true;
        }
    }
}

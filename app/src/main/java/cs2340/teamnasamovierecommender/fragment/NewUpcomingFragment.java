package cs2340.teamnasamovierecommender.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.activity.MovieDetailActivity;
import cs2340.teamnasamovierecommender.pojo.JsonTask;
import cs2340.teamnasamovierecommender.pojo.Movie;
import cs2340.teamnasamovierecommender.pojo.MovieAdapter;

/**
 * This class handles the In Theaters fragment. The user gets here by tapping on the in theaters tab
 * on the home screen.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class NewUpcomingFragment extends Fragment {

    ArrayList<Movie> movieArrayList;
    ListAdapter movieAdapter;
    private ListView inTheaters;
    private ProgressBar progressBar;
    private JsonTask task;
    SharedPreferences sharedPreferences;
    private JSONObject result;

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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_upcoming, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = sharedPreferences.getString("Username", "userDefault");

        Log.d("username", username);

        inTheaters = (ListView) view.findViewById(R.id.inTheatersList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        movieArrayList = new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(getContext(), movieArrayList);

        inTheaters.setAdapter(movieAdapter);
        inTheaters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieArrayList.get(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        task = new JsonTask(this);

        String baseURL = "http://api.themoviedb.org/3/movie/now_playing/";
        final String QUESTION = "?";
        final String API_KEY = "api_key=9910680035e7aaa74e54beea1da21ceb";
        String queryString = baseURL + QUESTION + API_KEY;
        Log.d("Query", queryString);

        task.execute(queryString);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.overflow_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void showProgressBar() {
        inTheaters.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    public void hideProgressBar() {
        inTheaters.setVisibility(View.VISIBLE);
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
                movieArrayList.add(movie);
            }
        } catch (JSONException e) {
            Log.d("JsonError", e.toString());
        }
    }

}

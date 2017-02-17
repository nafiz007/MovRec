package cs2340.teamnasamovierecommender.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * This class handles the Search fragment. The user gets here by tapping on the search tab on the
 * home screen.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    ArrayList<Movie> movieArrayList;
    ListAdapter movieAdapter;
    private ListView inTheaters;
    private ProgressBar progressBar;
    private JsonTask task;
    private JSONObject result;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_new_upcoming, container, false);

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
        return view;
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
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
            movieArrayList.clear();
            for (int i = 0; i < jsArrayResults.length(); i++) {
                String movieName = jsArrayResults.getJSONObject(i).getString("original_title");
                String movieYear = jsArrayResults.getJSONObject(i).getString("release_date");
                int movieId = Integer.parseInt(jsArrayResults.getJSONObject(i).getString("id"));
                String backdropPath = jsArrayResults.getJSONObject(i).getString("backdrop_path");
                Movie movie = new Movie(movieName, movieId, movieYear);
                movie.setBackdrop(backdropPath);
                Log.d("Movie", movie.toString());
                movieArrayList.add(movie);
            }
        } catch (JSONException e) {
            Log.d("JsonError", e.toString());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        task = new JsonTask(this);

        String baseURL = "http://api.themoviedb.org/3/search/movie/";
        final String API_KEY = "api_key=9910680035e7aaa74e54beea1da21ceb";
        final String QUESTION = "?";
        final String AMPERSAND = "&";

        query = query.replaceAll("\\s", " ");
        query = query.replaceAll("\\s", "+");
        String query4URL = "query=" + query;
        String queryURL = baseURL + QUESTION + API_KEY + AMPERSAND + query4URL;

        Log.d("Query", queryURL);

        task.execute(queryURL);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }
}

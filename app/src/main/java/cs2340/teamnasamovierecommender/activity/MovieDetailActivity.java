package cs2340.teamnasamovierecommender.activity;

/**
 * Created by sai on 4/20/16.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.CastAdapter;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView header;
    ListAdapter castAdapter;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    TextView description;
    ProgressBar progressBar;
    Movie movie;
    RatingBar ratingBar;
    ArrayList<HashMap<String, String>> castArray;
    ListView cast;
    DBHandler handler;
    String backdropUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = sharedPreferences.getString("Username", "userDefault");

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");

        handler = new DBHandler(this, username.toUpperCase() + ".db");


        header = (ImageView) findViewById(R.id.header);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ratingBar = (RatingBar) findViewById(R.id.rating);

        Double rating = handler.getRating(movie.getId());

        if (rating != null) {
            ratingBar.setRating(Float.valueOf(((float) (double) rating)));
        } else {
            Log.d("Rating", "DNE");
        }


        cast = (ListView) findViewById(R.id.castList);

        castArray = movie.getCast();

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        description = (TextView) findViewById(R.id.description);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        ActionBar actionBar = getSupportActionBar();

        try {
//            String base = "http://image.tmdb.org/t/p/w1280";
//            String url = base + backdropUrl;
//            ImageDownloader downloader = new ImageDownloader();
//            downloader.execute(url);

            JsonGetMovieData getMovieData = new JsonGetMovieData();
            getMovieData.execute("http://api.themoviedb.org/3/movie/" + movie.getId() + "?api_key=9910680035e7aaa74e54beea1da21ceb&append_to_response=credits");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (actionBar != null) {
            collapsingToolbar.setTitle(movie.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void addRating(View view) {
        double rating = ratingBar.getRating();
        handler.addRating(movie.getId(), movie.getName(), rating, movie.getRelease());
        Toast.makeText(getApplicationContext(), "Rating saved.", Toast.LENGTH_SHORT).show();
    }

    public void clearRating(View view) {
        ratingBar.setRating(0);
        handler.removeRating(movie.getId());
        Toast.makeText(getApplicationContext(), "Rating cleared.", Toast.LENGTH_SHORT).show();
    }


    private class JsonGetMovieData extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject result = null;
            Log.d("query", params[0]);

            try {
                StringBuilder response = new StringBuilder();

                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }
                result = new JSONObject(response.toString());
            } catch (Exception e) {
                Log.d("JsonTask", e.toString());
            }
            return result;
        }

        protected void onPostExecute(JSONObject result) {
            try {
                description.setText(result.getString("overview"));

                backdropUrl = result.getString("backdrop_path");

                movie.setRelease(result.getString("release_date"));

                String base = "http://image.tmdb.org/t/p/w1280";
                String url = base + backdropUrl;
                ImageDownloader downloader = new ImageDownloader();
                downloader.execute(url);

                JSONArray cast = result.getJSONObject("credits").getJSONArray("cast");

                for (int i = 0; i < 3; i++) {
                    String character = cast.getJSONObject(i).getString("character");
                    String name = cast.getJSONObject(i).getString("name");

                    movie.putInCast(character, name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            castAdapter = new CastAdapter(getApplicationContext(), castArray);
            cast.setAdapter(castAdapter);
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(url[0]).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(View.GONE);
            header.setImageBitmap(result);
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            header.startAnimation(myFadeInAnimation);
        }
    }

}


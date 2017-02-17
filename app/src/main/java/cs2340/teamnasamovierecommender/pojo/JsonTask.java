package cs2340.teamnasamovierecommender.pojo;

/**
 * Created by sai on 4/20/16.
 */
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cs2340.teamnasamovierecommender.fragment.NewUpcomingFragment;
import cs2340.teamnasamovierecommender.fragment.RecommendationsFragment;
import cs2340.teamnasamovierecommender.fragment.SearchFragment;

/**
 * Created by Sai on 3/26/2016.
 */
public class JsonTask extends AsyncTask<String, Void, JSONObject> {

    Fragment container;

    public JsonTask(Fragment container) {
        this.container = container;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (container instanceof NewUpcomingFragment) {
            ((NewUpcomingFragment) container).showProgressBar();
        }
        if (container instanceof SearchFragment) {
            ((SearchFragment) container).showProgressBar();
        }
        if (container instanceof RecommendationsFragment) {
            ((RecommendationsFragment) container).showProgressBar();
        }
    }

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
        super.onPostExecute(result);

        if (container != null && container.getActivity() != null) {
            if (container instanceof NewUpcomingFragment) {
                ((NewUpcomingFragment) container).populateList(result);
                ((NewUpcomingFragment) container).hideProgressBar();
            } else if (container instanceof SearchFragment) {
                ((SearchFragment) container).populateList(result);
                ((SearchFragment) container).hideProgressBar();
            } else if (container instanceof RecommendationsFragment) {
                ((RecommendationsFragment) container).populateList(result);
                ((RecommendationsFragment) container).hideProgressBar();
            }
            this.container = null;
        }
    }
}

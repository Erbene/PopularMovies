package com.erbene.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.util.Log;

import com.erbene.popularmovies.BuildConfig;
import com.erbene.popularmovies.R;
import com.erbene.popularmovies.SettingsActivity;
import com.erbene.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maia on 6/14/2016.
 */
public class GetMoviesTask extends AsyncTask<Void,Void, List<Movie>> {
    private final String LOG_TAG = "GetMoviesTask";
    private final String BY_POPULARITY = "http://api.themoviedb.org/3/movie/popular";
    private final String BY_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";

    private Callback mListener;
    private Context mContext;

    public GetMoviesTask(Callback cb, Context context){
        mListener = cb;
        mContext = context;
    }
    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mListener.onTaskCompleted(movies);
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        List<Movie> movies = null;
        String result = null;

        try {
            String base_url = null;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String mPrefOrder = sharedPref.getString(SettingsActivity.KEY_ORDER_BY, "");
            if(mPrefOrder.equals(mContext.getResources().getString(R.string.pref_order_top_rated))){
                base_url = BY_TOP_RATED;
            } else if (mPrefOrder.equals(mContext.getResources().getString(R.string.pref_order_popularity))){
                base_url = BY_POPULARITY;
            } else {
                base_url = BY_POPULARITY;
            }
            Uri builtUri = Uri.parse(base_url).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            result = buffer.toString();
            movies = parseJSON(result);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return movies;
    }
    private List<Movie> parseJSON(String json) throws JSONException{
        JSONObject searchInfo = new JSONObject(json);
        JSONArray moviesArray = searchInfo.getJSONArray("results");
        List<Movie> movieList = new ArrayList<>();
        for(int i = 0; i < moviesArray.length(); i++){
            movieList.add(new Movie(moviesArray.getJSONObject(i)));
        }
        return movieList;
    }
    public interface Callback {
        void onTaskCompleted(List<Movie> movies);
    }
}
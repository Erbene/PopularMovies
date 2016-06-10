package com.erbene.popularmovies;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.erbene.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;
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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final String TAG = "MovieListAdapter";

    private final String BASE_IMG_PATH = "http://image.tmdb.org/t/p/w185";

    public List<Movie> mMovieList;
    Context mContext;
    Callbacks mCallbackReceiver;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener;


    public MovieListAdapter(Context context, Callbacks cb){
        mContext = context;
        mCallbackReceiver = cb;
        mMovieList = new ArrayList<>();
        GetMoviesTask mTask = new GetMoviesTask();
        //listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(SettingsActivity.KEY_ORDER_BY)){
                    resetAdapter();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);

        mTask.execute();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie temp = mMovieList.get(position);
        temp.setPosterPath(BASE_IMG_PATH + temp.getPosterPath());
        final Movie mMovie = temp;
        Picasso.with(mContext).load(mMovie.getPosterPath()).placeholder(R.drawable.placeholder)
                .into(holder.mPosterView);
        holder.mPosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackReceiver.onClick(mMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterView;
        public ViewHolder(View v) {
            super(v);
            mPosterView = (ImageView) v.findViewById(R.id.movie_poster);
        }
    }
    public void resetAdapter(){
        mMovieList.clear();
        GetMoviesTask mTask = new GetMoviesTask();
        mTask.execute();
        notifyDataSetChanged();
    }
    public class GetMoviesTask extends AsyncTask<Void,Void, List<Movie>> {
        private final String LOG_TAG = "GetMoviesTask";
        private final String BY_POPULARITY = "http://api.themoviedb.org/3/movie/popular";
        private final String BY_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";


        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMovieList = movies;
            notifyDataSetChanged();
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
    }
    public interface Callbacks {
        public void onClick(Movie movie);
    }
}

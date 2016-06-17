package com.erbene.popularmovies.adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erbene.popularmovies.BuildConfig;
import com.erbene.popularmovies.R;
import com.erbene.popularmovies.SettingsActivity;
import com.erbene.popularmovies.data.MovieColumns;
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

public class MovieListAdapter extends CursorRecyclerViewAdapter<MovieListAdapter.ViewHolder> {

    private final String TAG = "MovieListAdapter";

    private final String BASE_IMG_PATH = "http://image.tmdb.org/t/p/w185";

    Context mContext;
    Callbacks mCallbackReceiver;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener;


    public MovieListAdapter(Context context, Callbacks cb, Cursor cursor){
        super(context,cursor);
        mContext = context;
        mCallbackReceiver = cb;

        //listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(SettingsActivity.KEY_ORDER_BY)){

                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        Movie temp = new Movie(cursor,false);
        temp.setPosterPath(BASE_IMG_PATH + temp.getPosterPath());
        final Movie mMovie = temp;
        Picasso.with(mContext).load(mMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.mPosterView);
        holder.mPosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackReceiver.onClick(mMovie);
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterView;
        public ViewHolder(View v) {
            super(v);
            mPosterView = (ImageView) v.findViewById(R.id.movie_poster);
        }
    }
    public interface Callbacks {
        public void onClick(Movie movie);
    }
}

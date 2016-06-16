package com.erbene.popularmovies;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.erbene.popularmovies.adapters.MovieListAdapter;
import com.erbene.popularmovies.data.GetMoviesTask;
import com.erbene.popularmovies.data.MovieColumns;
import com.erbene.popularmovies.data.MovieProvider;
import com.erbene.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListAdapter.Callbacks, LoaderManager.LoaderCallbacks<Cursor>,GetMoviesTask.Callback {

    private final String TAG = "MovieListFragment";
    private static final int CURSOR_LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public MovieListFragment() {

    }
    public void restartLoader(){
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(SettingsActivity.KEY_ORDER_BY)){
                    restartLoader();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Cursor c = null;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mPrefOrder = sharedPref.getString(SettingsActivity.KEY_ORDER_BY, "");
        if(mPrefOrder.equals(getResources().getString(R.string.pref_order_top_rated))){
            c = getActivity().getContentResolver().query(MovieProvider.TopRatedMovies.CONTENT_URI, null, null, null, null);
        } else if (mPrefOrder.equals(getResources().getString(R.string.pref_order_popularity))){
            c = getActivity().getContentResolver().query(MovieProvider.PopularityMovies.CONTENT_URI, null, null, null, null);
        }

        if (c == null || c.getCount() == 0){
            GetMoviesTask loadMoviesTask = new GetMoviesTask(this, getContext());
            loadMoviesTask.execute();
        }

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieListAdapter(getContext(),this,null);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onResume() {
        Log.i(TAG,"OnResume");
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(Movie movie) {
        Log.i("MovieList","OnClickEvent");
        Bundle parcel = new Bundle();
        parcel.putParcelable("movie",movie);
        MovieDetailFragment details = new MovieDetailFragment();
        details.setArguments(parcel);
        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.fragment_movie_list, details).addToBackStack(null).commit();
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        Uri destination = null;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mPrefOrder = sharedPref.getString(SettingsActivity.KEY_ORDER_BY, "");
        if(mPrefOrder.equals(getResources().getString(R.string.pref_order_popularity))){
            destination = MovieProvider.PopularityMovies.CONTENT_URI;
        } else if (mPrefOrder.equals(getResources().getString(R.string.pref_order_top_rated))){
            destination = MovieProvider.TopRatedMovies.CONTENT_URI;
        }
        for (Movie movie : movies){
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    destination);
            builder.withValue(MovieColumns._ID, movie.getId());
            builder.withValue(MovieColumns.OVERVIEW, movie.getOverview());
            builder.withValue(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
            builder.withValue(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
            builder.withValue(MovieColumns.POSTER_PATH, movie.getPosterPath());
            builder.withValue(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
            batchOperations.add(builder.build());
        }

        try{
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch(RemoteException | OperationApplicationException e){
            Log.e(TAG, "Error applying batch insert", e);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mPrefOrder = sharedPref.getString(SettingsActivity.KEY_ORDER_BY, "");
        CursorLoader cursor = null;
        if(mPrefOrder.equals(getResources().getString(R.string.pref_order_top_rated))){
            cursor = new CursorLoader(getActivity(), MovieProvider.TopRatedMovies.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } else if (mPrefOrder.equals(getResources().getString(R.string.pref_order_popularity))){
            cursor = new CursorLoader(getActivity(), MovieProvider.PopularityMovies.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites:
                FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(FavoritesFragment.TAG);
                if (fragment == null) {
                    fTransaction.add(R.id.fragment_movie_list, new FavoritesFragment(), FavoritesFragment.TAG);
                    fTransaction.addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
                break;
        }
        return true;
    }
}

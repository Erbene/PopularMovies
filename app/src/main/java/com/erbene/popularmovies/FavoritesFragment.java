package com.erbene.popularmovies;


import android.content.ContentProviderOperation;
import android.content.Context;
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
import android.widget.TextView;

import com.erbene.popularmovies.adapters.FavoriteListAdapter;
import com.erbene.popularmovies.adapters.MovieListAdapter;
import com.erbene.popularmovies.data.GetMoviesTask;
import com.erbene.popularmovies.data.MovieColumns;
import com.erbene.popularmovies.data.MovieProvider;
import com.erbene.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maia on 6/14/2016.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FavoriteListAdapter.Callbacks {

    public static final String TAG = "FavoritesFragmentXz";
    private static final int CURSOR_LOADER_ID = 1;

    private RecyclerView mRecyclerView;
    private TextView mEmptyFavorites;
    private FavoriteListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public FavoritesFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Cursor c = getActivity().getContentResolver().query(MovieProvider.FavoriteMovies.CONTENT_URI, null, null, null, null);
        View mView = inflater.inflate(R.layout.favorite_list, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.favorite_list_view);
        mEmptyFavorites = (TextView) mView.findViewById(R.id.no_favorites);

        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (c == null || c.getCount() == 0){
            mEmptyFavorites.setVisibility(View.VISIBLE);
        }
        mAdapter = new FavoriteListAdapter(getContext(),this,null);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.favorites);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

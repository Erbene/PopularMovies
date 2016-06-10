package com.erbene.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erbene.popularmovies.models.Movie;

public class MovieListFragment extends Fragment implements MovieListAdapter.Callbacks {

    private final String TAG = "MovieListFragment";

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieListAdapter(getContext(),this);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }


    @Override
    public void onResume() {
        Log.i(TAG,"OnResume");
        super.onResume();
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
}

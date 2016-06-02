package com.erbene.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erbene.popularmovies.models.Movie;


/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            getActivity().getFragmentManager().popBackStack();
        }
        else {
            mMovie = savedInstanceState.getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        super.onSaveInstanceState(outState);
    }
}

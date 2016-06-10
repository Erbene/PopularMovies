package com.erbene.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbene.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;


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
        mMovie = getArguments().getParcelable("movie");
        TextView textView = (TextView) view.findViewById(R.id.original_title);
        textView.setText(mMovie.getOriginalTitle());
        textView = (TextView) view.findViewById(R.id.overview);
        textView.setText(mMovie.getOverview());
        textView = (TextView) view.findViewById(R.id.release_date);
        textView.setText(mMovie.getReleaseDate());
        Picasso.with(getContext()).load(mMovie.getPosterPath())
                .into((ImageView) view.findViewById(R.id.poster_detail));
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        super.onSaveInstanceState(outState);
    }
}

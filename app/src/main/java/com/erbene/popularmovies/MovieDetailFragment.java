package com.erbene.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbene.popularmovies.adapters.VideoListAdapter;
import com.erbene.popularmovies.data.MovieColumns;
import com.erbene.popularmovies.data.MovieProvider;
import com.erbene.popularmovies.models.Movie;
import com.erbene.popularmovies.models.Video;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener, VideoListAdapter.Callbacks {

    private Movie mMovie;
    public static final String TAG ="MovieDetailFragment";
    private Button mFavoriteButton;
    private VideoListAdapter mVideoListAdapter;
    private RecyclerView mVideoListView;
    private LinearLayoutManager mLayoutManager;

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
        mVideoListAdapter = new VideoListAdapter(getContext(),this,mMovie.getId());
        checkIfFavorite();
        TextView textView = (TextView) view.findViewById(R.id.original_title);
        textView.setText(mMovie.getOriginalTitle());
        textView = (TextView) view.findViewById(R.id.overview);
        textView.setText(mMovie.getOverview());
        textView = (TextView) view.findViewById(R.id.release_date);
        textView.setText(mMovie.getReleaseDate());
        mFavoriteButton = (Button) view.findViewById(R.id.favorite_button);
        updateButtonInterface();
        mFavoriteButton.setOnClickListener(this);
        Picasso.with(getContext()).load(mMovie.getPosterPath())
                .into((ImageView) view.findViewById(R.id.poster_detail));
        mVideoListView = (RecyclerView) view.findViewById(R.id.videos_list_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mVideoListView.setLayoutManager(mLayoutManager);
        mVideoListView.setAdapter(mVideoListAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onClick(View v) {
        Log.i(TAG,"put on favorites");
        try {

            if(!mMovie.isFavorite()) {
                ContentValues cv = new ContentValues();
                cv.put(MovieColumns.POSTER_PATH, mMovie.getPosterPath());
                cv.put(MovieColumns.RELEASE_DATE, mMovie.getReleaseDate());
                cv.put(MovieColumns.ORIGINAL_TITLE, mMovie.getOriginalTitle());
                cv.put(MovieColumns._ID, mMovie.getId());
                cv.put(MovieColumns.OVERVIEW, mMovie.getOverview());
                getContext().getContentResolver().insert(MovieProvider.FavoriteMovies.withId(mMovie.getId()),
                        cv);
                mMovie.setFavorite(true);

            } else {
                getContext().getContentResolver().delete(MovieProvider.FavoriteMovies.withId(mMovie.getId()),
                        null,null);
                mMovie.setFavorite(false);
            }
            updateButtonInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void updateButtonInterface(){
        if(mFavoriteButton == null) return;
        if(mMovie.isFavorite()){
            mFavoriteButton.setText(R.string.remove_favorites_button);
        } else {
            mFavoriteButton.setText(R.string.add_favorites_button);
        }
    }
    private void checkIfFavorite(){
        if(mMovie == null) return;
        Cursor c = getActivity().getContentResolver().query(MovieProvider.FavoriteMovies.withId(mMovie.getId()), null, null, null, null);
        if(c == null || c.getCount() == 0){
            mMovie.setFavorite(false);
        } else {
            mMovie.setFavorite(true);
        }
    }

    @Override
    public void onClick(Video video) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +video.getKey())));
    }
}

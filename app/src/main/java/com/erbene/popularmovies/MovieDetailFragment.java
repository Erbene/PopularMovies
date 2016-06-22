package com.erbene.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.erbene.popularmovies.adapters.ReviewListAdapter;
import com.erbene.popularmovies.adapters.VideoListAdapter;
import com.erbene.popularmovies.data.MovieColumns;
import com.erbene.popularmovies.data.MovieProvider;
import com.erbene.popularmovies.models.Movie;
import com.erbene.popularmovies.models.Review;
import com.erbene.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener, VideoListAdapter.Callbacks, ReviewListAdapter.Callbacks {

    private Movie mMovie;
    public static final String TAG ="MovieDetailFragment";
    private Button mFavoriteButton;
    private VideoListAdapter mVideoListAdapter;
    private RecyclerView mVideoListView;
    private ReviewListAdapter mReviewListAdapter;
    private RecyclerView mReviewListView;
    private LinearLayoutManager mVideoLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;
    private TextView mNoVideos;
    private TextView mNoReviews;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("sdfs","sdfs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        //If there are no arguments just show no movie info.
        if(getArguments() == null){
            ScrollView sv = (ScrollView) view.findViewById(R.id.scroll_wrapper);
            sv.setVisibility(View.GONE);
            TextView tv = (TextView) view.findViewById(R.id.no_movie_info);
            tv.setVisibility(View.VISIBLE);
            return view;
        }

        mMovie = getArguments().getParcelable(Movie.MOVIE_URI);

        mVideoListAdapter = new VideoListAdapter(getContext(),this,mMovie.getId());
        mReviewListAdapter = new ReviewListAdapter(getContext(),this,mMovie.getId());
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
        mVideoLayoutManager = new LinearLayoutManager(getContext());
        mVideoListView.setLayoutManager(mVideoLayoutManager);
        mVideoListView.setAdapter(mVideoListAdapter);

        mReviewListView = (RecyclerView) view.findViewById(R.id.reviews_list_view);
        mReviewLayoutManager = new LinearLayoutManager(getContext());
        mReviewListView.setLayoutManager(mReviewLayoutManager);
        mReviewListView.setAdapter(mReviewListAdapter);

        mNoReviews = (TextView) view.findViewById(R.id.no_reviews_text);
        mNoVideos = (TextView) view.findViewById(R.id.no_videos_text);
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

    @Override
    public void onVideosRetrieved(List<Video> videos) {
        if(videos.size() == 0) mNoVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviewsRetrieved(List<Review> reviews) {
        if(reviews.size() == 0) mNoReviews.setVisibility(View.VISIBLE);
    }
}

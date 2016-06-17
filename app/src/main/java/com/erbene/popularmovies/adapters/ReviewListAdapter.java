package com.erbene.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erbene.popularmovies.BuildConfig;
import com.erbene.popularmovies.R;
import com.erbene.popularmovies.models.Review;
import com.erbene.popularmovies.models.ReviewResponse;
import com.erbene.popularmovies.models.Video;
import com.erbene.popularmovies.rest.MovieDbApiInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maia on 6/16/2016.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> implements Callback<ReviewResponse> {
    private final String TAG = "ReviewListAdapter";

    public List<Review> mReviewList;
    Context mContext;
    MovieDbApiInterface mMovieDbService;
    Long mMovieId;
    Callbacks mListener;


    public ReviewListAdapter(Context context,Callbacks cb, Long movieId){
        mContext = context;
        mReviewList = new ArrayList<>();
        mMovieId = movieId;
        mListener = cb;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbApiInterface.BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMovieDbService =
                retrofit.create(MovieDbApiInterface.class);
        retrieveReviews();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.mAuthorName.setText(review.getAuthor());
        holder.mAuthorContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorName;
        public TextView mAuthorContent;
        public ViewHolder(View v) {
            super(v);
            mAuthorName = (TextView) v.findViewById(R.id.author_name);
            mAuthorContent = (TextView) v.findViewById(R.id.author_content);
        }
    }

    public void retrieveReviews(){
        Call<ReviewResponse> call = mMovieDbService.getReviews(mMovieId.toString(), BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(this);
    }

    public interface Callbacks {
        public void onReviewsRetrieved(List<Review> reviews);
    }
    @Override
    public void onFailure(Call<ReviewResponse> call, Throwable t) {

    }

    @Override
    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
        ReviewResponse vResponse = response.body();
        mReviewList = vResponse.getReviews();
        mListener.onReviewsRetrieved(mReviewList);
        notifyDataSetChanged();
    }
}
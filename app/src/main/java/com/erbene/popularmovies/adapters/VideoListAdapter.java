package com.erbene.popularmovies.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbene.popularmovies.BuildConfig;
import com.erbene.popularmovies.R;
import com.erbene.popularmovies.SettingsActivity;
import com.erbene.popularmovies.data.GetMoviesTask;
import com.erbene.popularmovies.models.Video;
import com.erbene.popularmovies.models.VideoResponse;
import com.erbene.popularmovies.rest.MovieDbApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maia on 6/15/2016.
 */
public class VideoListAdapter  extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements Callback<VideoResponse> {
    private final String TAG = "VideoListAdapter";

    public List<Video> mVideoList;
    Context mContext;
    Callbacks mCallbackReceiver;
    MovieDbApiInterface mMovieDbService;
    Long mMovieId;


    public VideoListAdapter(Context context, Callbacks cb, Long movieId){
        mContext = context;
        mCallbackReceiver = cb;
        mVideoList = new ArrayList<>();
        mMovieId = movieId;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbApiInterface.BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMovieDbService =
                retrofit.create(MovieDbApiInterface.class);
        retrieveVideos();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video temp = mVideoList.get(position);
        final Video mVideo = temp;
        Picasso.with(mContext).load(R.drawable.play_blue).placeholder(R.drawable.placeholder)
                .into(holder.mIconView);
        holder.mIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackReceiver.onClick(mVideo);
            }
        });
        holder.mVideoName.setText(temp.getName());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIconView;
        public TextView mVideoName;
        public ViewHolder(View v) {
            super(v);
            mIconView = (ImageView) v.findViewById(R.id.play_icon);
            mVideoName = (TextView) v.findViewById(R.id.video_name);
        }
    }

    public void retrieveVideos(){
        Call<VideoResponse>  call = mMovieDbService.getVideos(mMovieId.toString(),BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(this);
    }

    public interface Callbacks {
        public void onClick(Video video);
    }

    @Override
    public void onFailure(Call<VideoResponse> call, Throwable t) {

    }

    @Override
    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
        VideoResponse vResponse = response.body();
        mVideoList = vResponse.getVideos();
        notifyDataSetChanged();
    }
}

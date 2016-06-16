package com.erbene.popularmovies.rest;

import com.erbene.popularmovies.models.ReviewResponse;
import com.erbene.popularmovies.models.VideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Maia on 6/15/2016.
 */
public interface MovieDbApiInterface {

    public static final String BASE_PATH = "http://api.themoviedb.org/3/";

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String id, @Query("api_key") String apiKey);

     @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") String id, @Query("api_key") String apiKey);
}
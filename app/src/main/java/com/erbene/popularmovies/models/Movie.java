package com.erbene.popularmovies.models;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Maia on 5/30/2016.
 */
public class Movie {
    String mPosterPath;
    boolean mAdult;
    String mOverview;
    String mReleaseDate;
    List<Integer> mGenderList;
    Long mId;
    String mOriginalTitle;
    String mOriginalLanguage;
    String mTitle;
    String mBackdropPath;
    Float mPopularity;
    Integer mVoteCount;
    boolean mVideo;
    Double mVoteAverage;
/*    For the moment, will only set requested parameters.
    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date
 */
    public Movie(JSONObject json){
        try {
            setOriginalTitle(json.getString("original_title"));
            setPosterPath(json.getString("poster_path"));
            setOverview(json.getString("overview"));
            setVoteAverage(json.getDouble("vote_average"));
            setReleaseDate(json.getString("release_date"));
        } catch(Exception e){

        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return mId.equals(movie.mId);

    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public List<Integer> getGenderList() {
        return mGenderList;
    }

    public void setGenderList(List<Integer> mGenderList) {
        this.mGenderList = mGenderList;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String mOriginalLanguage) {
        this.mOriginalLanguage = mOriginalLanguage;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public Integer getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(Integer mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public Float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Float mPopularity) {
        this.mPopularity = mPopularity;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }
}

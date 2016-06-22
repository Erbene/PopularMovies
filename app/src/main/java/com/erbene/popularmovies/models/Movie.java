package com.erbene.popularmovies.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.TtsSpan;
import android.util.Log;

import com.erbene.popularmovies.data.MovieColumns;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Maia on 5/30/2016.
 */
public class Movie implements Parcelable {
    public static final String MOVIE_URI = "movie";
    String mPosterPath;
    boolean mAdult;
    String mOverview;
    String mReleaseDate;
    List<Integer> mGenderList;
    long mId;
    String mOriginalTitle;
    String mOriginalLanguage;
    String mTitle;
    String mBackdropPath;
    Float mPopularity;
    Integer mVoteCount;
    boolean mVideo;
    Double mVoteAverage;
    boolean mFavorite;
/*    For the moment, will only set requested parameters.
    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date
 */
    public Movie(JSONObject json){
        try {
            Log.i("Movie","Movie created: "+json.getString("original_title") );
            setId(json.getInt("id"));
            setOriginalTitle(json.getString("original_title"));
            setPosterPath(json.getString("poster_path"));
            setOverview(json.getString("overview"));
            setVoteAverage(json.getDouble("vote_average"));
            setReleaseDate(json.getString("release_date"));
        } catch(Exception e){

        }
    }
    public Movie(Cursor cursor, boolean favorite){
        try {
            setId(new Integer(cursor.getInt(cursor.getColumnIndex(MovieColumns._ID))));
            setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
            setPosterPath(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH)));
            setOverview(cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW)));
            setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
            mFavorite = favorite;
            setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
        } catch(Exception e){

        }
    }
    public Movie(Parcel pc) {
        setOriginalTitle(pc.readString());
        setPosterPath(pc.readString());
        setOverview(pc.readString());
        setReleaseDate(pc.readString());
        setVoteAverage(pc.readDouble());
        setId(pc.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getOriginalTitle());
        dest.writeString(getPosterPath());
        dest.writeString(getOverview());
        dest.writeString(getReleaseDate());
        dest.writeDouble(getVoteAverage());
        dest.writeLong(getId());
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel pc) {
            return new Movie(pc);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return mId == (movie.mId);

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

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
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
    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean mFavorite) {
        this.mFavorite = mFavorite;
    }
}

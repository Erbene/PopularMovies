package com.erbene.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Maia on 6/12/2016.
 */
@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {

        public static final int VERSION = 1;

        @Table(MovieColumns.class) public static final String MOVIE_BY_POPULARITY = "movie_popularity";
        @Table(MovieColumns.class) public static final String MOVIE_BY_TOP_RATED = "movie_top_rated";
        @Table(MovieColumns.class) public static final String FAVORITE_MOVIE = "favorite_movie";
}

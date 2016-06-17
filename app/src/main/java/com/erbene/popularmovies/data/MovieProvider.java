package com.erbene.popularmovies.data;

import android.net.Uri;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;
/**
 * Created by Maia on 6/11/2016.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {
    public static final String AUTHORITY = "com.erbene.popularmovies.data.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String POPULARITY = "popularity";
        String TOP_RATED = "top_rated";
        String FAVORITE = "favorite";
    }
    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = MovieDatabase.MOVIE_BY_POPULARITY) public static class PopularityMovies {
        @ContentUri(
                path = Path.POPULARITY,
                type = "vnd.android.cursor.dir/movie"
                )
        public static final Uri CONTENT_URI = buildUri(Path.POPULARITY);

        @InexactContentUri(
                path = Path.POPULARITY+"/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.POPULARITY, String.valueOf(id));
        }
    }
    @TableEndpoint(table = MovieDatabase.MOVIE_BY_TOP_RATED) public static class TopRatedMovies {
        @ContentUri(
                path = Path.TOP_RATED,
                type = "vnd.android.cursor.dir/movie"
                )
        public static final Uri CONTENT_URI = buildUri(Path.TOP_RATED);

        @InexactContentUri(
                path = Path.TOP_RATED+"/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.TOP_RATED, String.valueOf(id));
        }
    }
    @TableEndpoint(table = MovieDatabase.FAVORITE_MOVIE) public static class FavoriteMovies {
        @ContentUri(
                path = Path.FAVORITE,
                type = "vnd.android.cursor.dir/movie"
                )
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITE);

        @InexactContentUri(
                path = Path.FAVORITE+"/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.FAVORITE, String.valueOf(id));
        }
    }

}

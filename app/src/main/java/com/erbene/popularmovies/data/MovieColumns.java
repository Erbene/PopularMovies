package com.erbene.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Maia on 6/12/2016.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey String _ID = "_id";
    @DataType(DataType.Type.TEXT) String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT) String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.TEXT) String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT) String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.REAL) String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.REAL) String POPULARITY = "popularity";
}

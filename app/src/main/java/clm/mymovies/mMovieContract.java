package clm.mymovies;

import android.net.Uri;

/**
 * Created by CLM on 8/9/2016.
 */
public class mMovieContract {

    public static class mMovieTable
    {
        static String DB_TABLE="Movies";
        static int DB_VERSION=12;

        static String DB_MOVIE_NAME="Name";
        static String DB_MOVIE_DESC="Description";
        static String DB_MOVIE_URL="LINK";
        static String DB_MOVIE_IMAGE="Image";

        public  static String TableName= DB_TABLE;
        public  static String Authority= "clm.mymovies";
        public static Uri ContentURI=Uri.parse("content://"+Authority+"/"+TableName);
    }

}

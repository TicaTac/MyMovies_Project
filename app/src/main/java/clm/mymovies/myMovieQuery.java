package clm.mymovies;

import android.graphics.Bitmap;

/**
 * Created by CLM on 7/25/2016.
 */
public class myMovieQuery {

    String Title;
    String Year;
    String imdbID;
    String Type;
    String PosterURL;
    Bitmap image;

    public myMovieQuery(String title, String year, String imdbID, String type, String posterURL,Bitmap image) {
        Title = title;
        Year = year;
        this.imdbID = imdbID;
        Type = type;
        PosterURL = posterURL;
        this.image=image;
    }

/*
    String[] a;
    String[] search;
    String title;*/

}

package clm.mymovies;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by CLM on 7/24/2016.
 */
public class myMovieDB {
    int _id;
    String name;//one line
    String description;// movie description. multiple lines
    String url; //
    Bitmap image;
    int Rating;
    int Seen;
    String omdbID;
    

    public myMovieDB(String name, String description, String url, Bitmap image) {
        Log.d("myMovieDB: ","Create Record "+name+description+url);
        this.name = name;
        this.description = description;
        this.url = url;
        this.image=image;
        //Rating=0;
        //Seen=0;
        
        
    }
}

package clm.mymovies;

import android.util.Log;

/**
 * Created by CLM on 7/24/2016.
 */
public class myMovie {
    int _id;
    String name;//one line
    String body;// movie description. multiple lines
    String url; //

    public myMovie() {
        this.name=null;
        this.body=null;
        this.name=null;
    }

    public myMovie(String name, String body, String url) {
        Log.d("myMovie: ","Create Record "+name+body+url);
        this.name = name;
        this.body = body;
        this.url = url;
    }
}

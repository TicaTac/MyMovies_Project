package clm.mymovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lee on 26/07/2016.
 */
public class mySearchAdapter extends ArrayAdapter<myMovieQuery> {
    List<myMovieQuery> movieQueryList;
    Context c;

    public mySearchAdapter(Context context, int resource,  List<myMovieQuery> objects) {
        super(context, resource,  objects);
        movieQueryList=objects;
        this.c=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(c);
            v= inflater.inflate(R.layout.single_movie_search_item, null);
        }


        TextView searchMovieNameTV= (TextView) v.findViewById(R.id.searchMovieNameTV);
        ImageView searchItemImageIV = (ImageView) v.findViewById(R.id.searchItemIV);

        myMovieQuery tempMovieQuery = movieQueryList.get(position);

        searchMovieNameTV.setText(tempMovieQuery.Title);

        Log.d("Adapter","SetImage : "+tempMovieQuery.Title);

        if (tempMovieQuery.image!=null) {
            searchItemImageIV.setImageBitmap(tempMovieQuery.image);
        }


        return v;
    }


}

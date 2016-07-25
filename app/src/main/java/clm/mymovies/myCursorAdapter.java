package clm.mymovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by CLM on 7/25/2016.
 */
public class myCursorAdapter extends CursorAdapter {

    public myCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.single_movie_item,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV=(TextView) view.findViewById(R.id.nameTV);
        TextView descTV=(TextView) view.findViewById(R.id.descTV);
        ImageView imageIV=(ImageView) view.findViewById(R.id.imageIV);

        myCommands commands=new myCommands(context);

        nameTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(myConstants.DB_MOVIE_NAME)));
        descTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(myConstants.DB_MOVIE_DESC)));

        String imageBase64=cursor.getString(cursor.getColumnIndexOrThrow(myConstants.DB_MOVIE_IMAGE));
        if (imageBase64!=null) imageIV.setImageBitmap(commands.decodeBase64(imageBase64));


    }
}

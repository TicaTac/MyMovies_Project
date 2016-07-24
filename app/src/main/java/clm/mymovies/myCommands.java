package clm.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by CLM on 7/24/2016.
 */
public class myCommands {

    myDbHelper helper;
    Context c;


    public myCommands(Context c) {
        this.c = c;
        helper=new myDbHelper(c);
    }


    public Cursor getDbQuery()
    {
        return helper.getReadableDatabase().query(myConstants.DB_TABLE,null,null,null,null,null,null);
    }

    public Cursor getDbQuery(int dbID)
    {
        return helper.getReadableDatabase().query(myConstants.DB_TABLE,null,null,null,null,null,null);
    }

    public void addDb(myMovie movie)
    {
        ContentValues cv=new ContentValues();
        cv.put(myConstants.DB_MOVIE_NAME,movie.name);
        cv.put(myConstants.DB_MOVIE_DESC,movie.body);
        cv.put(myConstants.DB_MOVIE_URL,movie.url);

        helper.getWritableDatabase().insert(myConstants.DB_TABLE,null,cv);
    }

    public boolean deleteDb(myMovie movie)
    {
        int dbID=movie._id;
     /*   Cursor c= helper.getReadableDatabase().query(myConstants.DB_TABLE,null,myConstants.DB_ID,new String[]{""+dbID},null,null,null);
        if (c.moveToNext()){
            if (c.getString(c.getColumnIndexOrThrow(myConstants.DB_NAME)).equals(movie.name)&&
                    ){

            }
        }*/

       return (helper.getWritableDatabase().delete(myConstants.DB_TABLE,myConstants.DB_ID+"=?",new String[]{""+dbID})>0);

    }
}

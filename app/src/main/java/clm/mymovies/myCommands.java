package clm.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

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

        Log.d("DB ","Read Full DB");
        Cursor c=helper.getReadableDatabase().query(myConstants.DB_TABLE,null,null,null,null,null,null);
//        Log.d("Read Full DB","..");
        return c;
    }

    public Cursor getDbQuery(int dbID)
    {
        Log.d("DB ","Read DB ID "+dbID);
        Cursor c=helper.getReadableDatabase().query(myConstants.DB_TABLE,null,myConstants.DB_ID+"=?",new String[]{""+dbID},null,null,null);
        return c;
    }

    public boolean clearDB()
    {
        return helper.getWritableDatabase().delete(myConstants.DB_TABLE,"1",null)>0;
    }
    public void addDb(myMovie movie)
    {
        ContentValues cv=new ContentValues();
        cv.put(myConstants.DB_MOVIE_NAME,movie.name);
        cv.put(myConstants.DB_MOVIE_DESC,movie.body);
        cv.put(myConstants.DB_MOVIE_URL,movie.url);
        Log.d("DB ","Add :"+movie.name);
        helper.getWritableDatabase().insert(myConstants.DB_TABLE,null,cv);
    }
    public void Logger(String s)
    {
        Log.d("_DB",s);
    }

    public void toaster(Context c,String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }
    public boolean deleteDb(int dbID)
    {


        return (helper.getWritableDatabase().delete(myConstants.DB_TABLE,myConstants.DB_ID+"=?",new String[]{""+dbID})>0);

    }
}
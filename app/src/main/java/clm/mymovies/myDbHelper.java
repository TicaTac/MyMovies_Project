package clm.mymovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CLM on 7/24/2016.
 */
public class myDbHelper extends SQLiteOpenHelper{
    public myDbHelper(Context context) {
        super(context, myConstants.DB_NAME, null, myConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery
                ="CREATE TABLE"
                +myConstants.DB_TABLE+" ("
                +myConstants.DB_ID+" PRIMARY KEY AUTOINCREMENT,"
                +myConstants.DB_MOVIE_NAME+" TEXT,"
                +myConstants.DB_MOVIE_DESC+" TEXT,"
                +myConstants.DB_MOVIE_URL+" TEXT "
                +");";


        db.execSQL(createQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

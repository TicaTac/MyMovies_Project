package clm.mymovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                ="CREATE TABLE "
                +myConstants.DB_TABLE+" ( "
                +myConstants.DB_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +myConstants.DB_MOVIE_NAME+" TEXT , "
                +myConstants.DB_MOVIE_DESC+" TEXT , "
                +myConstants.DB_MOVIE_URL+" TEXT "
                +");";

        Log.d("DB ","Create DB: "+createQuery);
        db.execSQL(createQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<myConstants.DB_VERSION) {
            Log.d("DbHelper-","UpgradeDB oldVersion:"+oldVersion+" newVersion:"+newVersion);

            String alterQuery
                    = "ALTER TABLE "
                    + myConstants.DB_TABLE + " "
                    + "ADD COLUMN" + " "
                    + myConstants.DB_MOVIE_IMAGE + " BLOB";
            db.execSQL(alterQuery);
        }
    }
}

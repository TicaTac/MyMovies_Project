package clm.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

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

    public void addDb(myMovieDB movie)
    {
        ContentValues cv=new ContentValues();
        cv.put(myConstants.DB_MOVIE_NAME,movie.name);
        cv.put(myConstants.DB_MOVIE_DESC,movie.description);
        cv.put(myConstants.DB_MOVIE_URL,movie.url);
        if (movie.image!=null) cv.put(myConstants.DB_MOVIE_IMAGE,encodeToBase64(movie.image, Bitmap.CompressFormat.PNG,100));

        Log.d("DB ","Add :"+movie.name);

        helper.getWritableDatabase().insert(myConstants.DB_TABLE,null,cv);
    }

    public void updateDb(myMovieDB movie)
    {

        if (movie._id>=0) {
            ContentValues cv = new ContentValues();
            cv.put(myConstants.DB_MOVIE_NAME,movie.name);
            cv.put(myConstants.DB_MOVIE_DESC,movie.description);
            cv.put(myConstants.DB_MOVIE_URL , movie.url);
            cv.put(myConstants.DB_MOVIE_IMAGE,encodeToBase64(movie.image, Bitmap.CompressFormat.PNG,100));

            helper.getWritableDatabase().update(myConstants.DB_TABLE,cv,myConstants.DB_ID+"=?",new String[]{""+movie._id});
        }
    }
    public boolean clearDB()
    {
        return helper.getWritableDatabase().delete(myConstants.DB_TABLE,"1",null)>0;
    }

    public boolean deleteDb(int dbID)
    {


        return (helper.getWritableDatabase().delete(myConstants.DB_TABLE,myConstants.DB_ID+"=?",new String[]{""+dbID})>0);

    }


    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
       if (bitmap!=null) {
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
           return stream.toByteArray();
       }
        else return null;

    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
       if (image!=null) {
           return BitmapFactory.decodeByteArray(image, 0, image.length);
       } else return null;
    }

    public void saveImageToDB(int dbID){


    }
    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public void toaster(Context c,String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }

    public void Logger(String s)
    {
        Log.d("_DB",s);
    }
}

package clm.mymovies;

import android.animation.ObjectAnimator;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by CLM on 8/9/2016.
 */
public class mMoviesContentProvider extends ContentProvider {
    myDbHelper helper;
    @Override
    public boolean onCreate() {
        helper= new myDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        c=helper.getReadableDatabase().query(myConstants.DB_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

package clm.mymovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CLM on 7/27/2016.
 */
public class myGetImageHelper extends AsyncTask<String, String, Bitmap> {
    Bitmap image;

    @Override
    protected void onProgressUpdate(String... values) {
        Log.d("myGetImageHelper"," Progress Update"+values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);
        image=bmp;
        saveFile(bmp);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bmp = getBitmap(params);
        Log.d("getImage","Finished - "+params[0]);
        return bmp;
    }

/////////////////////////////////////////////////// END OF - ASYNC TASK /////////////////////////

    public Bitmap getBitmap(String... params){
        Bitmap bmp=null;
        // start connection
            /*BufferedReader input=null;
            HttpURLConnection connection=null;
            BufferedInputStream bis;*/
        try {
            bmp = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
/*
                URL url=new URL(params[0]);
                connection=(HttpURLConnection) url.openConnection();
                if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK) {
                    // connection not good return
                }
                //get a buffer reader to read the data stream as characters(letters)
                //in a buffered way.

                bis=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //decode the stream

                bmp= BitmapFactory.decodeStream(input);
                while ((line=input.readLine())!=null){
                    //append it to a StringBuilder to hold the
                    //resulting string
                    response.append(line+"\n");
                }*/


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        } finally{

        };

        return bmp;
    }
    public  void saveFile(Bitmap original)
    {
/*

        //Bitmap original = BitmapFactory.decodeStream(getAssets().open("1024x768.jpg"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

        Log.e("Original   dimensions", original.getWidth()+" "+original.getHeight());
        Log.e("Compressed dimensions", decoded.getWidth()+" "+decoded.getHeight());
*/
        Log.d("getImage","+ Not Saved ");
    }

}

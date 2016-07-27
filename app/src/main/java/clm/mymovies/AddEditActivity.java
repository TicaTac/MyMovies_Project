package clm.mymovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AddEditActivity extends AppCompatActivity {
    myCommands commands;
    EditText nameET;
    EditText descET;
    TextView progressTV;
    String url,name,desc;
    int dbID;
    myDbHelper helper;
    ImageView iv;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        image=null;
        commands=new myCommands(AddEditActivity.this);
        Intent intent=getIntent();
        dbID=intent.getIntExtra(myConstants.DB_ID,-2);
        helper=new myDbHelper(this);
        Log.d("DB ","Loading AddEditActivity dbID"+dbID);

        nameET=(EditText) findViewById(R.id.nameET);
        descET=(EditText) findViewById(R.id.descriptionET);
        iv=(ImageView) findViewById(R.id.thumbIV);
        progressTV=(TextView) findViewById(R.id.progressTV);

        commands.toaster(this,"dbID"+dbID);

        // get Source intent -
        // if dbID -1/-2 => new Item
        //  dbID >=0 then edit SQL record
        if (dbID>=0) {
            // load SQ: record
            Cursor c= commands.getDbQuery(dbID);
            if (c.moveToNext()) {
                nameET.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_NAME)));
                descET.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_DESC)));
                iv.setImageBitmap(commands.getImage(c.getBlob(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_IMAGE))));

            }
        }



        Button saveBTN=(Button) findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddEditActivity"," Add new Record to DB");
                // TODO: 7/24/2016 add update Record in DB

                name=nameET.getText().toString();
                desc=descET.getText().toString();
                url=null;

                if (dbID>=0) {
                    myMovieDB movie=new myMovieDB(name,desc,url, image);
                    commands.updateDb(movie);
                }
                else {
                    myMovieDB movie=new myMovieDB(name,desc,url, image);
                    commands.addDb(movie);
                }
                finish();

            }
        });

        Button cancelBTN= (Button) findViewById(R.id.cancelBTN);
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv = (ImageView) findViewById(R.id.thumbIV);

        Button showBTN = (Button) findViewById(R.id.showBTN);
        showBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText urlET=(EditText) findViewById(R.id.urlET) ;
                String url = urlET.getText().toString();

                DownloadImage loadImage = new DownloadImage(iv,null);

                loadImage.execute(url);

            }
        });


    }


    public class DownloadImage extends AsyncTask<String,Integer,Bitmap>{
        ImageView imageView;
        String saveFileLocation;

        public DownloadImage(ImageView imageView, String saveFileLocation) {
            this.imageView = imageView;
            this.saveFileLocation = saveFileLocation;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getImage(params);
        }

        private Bitmap getImage(String... params){
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
        protected  void saveFile(Bitmap original)
        {

            //Bitmap original = BitmapFactory.decodeStream(getAssets().open("1024x768.jpg"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            Log.e("Original   dimensions", original.getWidth()+" "+original.getHeight());
            Log.e("Compressed dimensions", decoded.getWidth()+" "+decoded.getHeight());

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressTV.setText(""+values[0]);
            Log.d("Read BMP","Progress "+values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            image=bmp;
            imageView.setImageBitmap(image);

            saveFile(bmp);

        }
    }
}

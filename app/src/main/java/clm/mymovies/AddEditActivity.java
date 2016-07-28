package clm.mymovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEditActivity extends AppCompatActivity {
    myCommands commands;
    EditText nameET;
    EditText descET;
    TextView progressTV;
    String url, name, desc;
    int dbID;
    String imdbID;
    myDbHelper helper;
    myGetJsonHelper jsonHelper;
    myGetImageHelper imageHelper;
    ImageView iv;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        image = null;
        commands = new myCommands(AddEditActivity.this);

        Intent intent = getIntent();
        dbID = intent.getIntExtra(myConstants.DB_ID, -2);
        imdbID = intent.getStringExtra(myConstants.DB_IMDB);

        helper = new myDbHelper(this);
        Log.d("DB ", "Loading AddEditActivity dbID" + dbID);
        jsonHelper = new myGetJsonHelper();
        imageHelper = new myGetImageHelper();

        nameET = (EditText) findViewById(R.id.nameET);
        descET = (EditText) findViewById(R.id.descriptionET);
        iv = (ImageView) findViewById(R.id.thumbIV);
      //  progressTV = (TextView) findViewById(R.id.progressTV);

        // commands.toaster(this, "dbID " + dbID);

        if (imdbID != null) {

            String url = myConstants.OMDB_ITEM_IMDB_QUERY + imdbID;
            GetJSONTask getJsonWithIMDB = new GetJSONTask();
            getJsonWithIMDB.execute(url);

        } else if (dbID >= 0) {

            // load SQL record
            Cursor c = commands.getDbQuery(dbID);
            if (c.moveToNext()) {
                nameET.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_NAME)));
                descET.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_DESC)));
                iv.setImageBitmap(commands.getImage(c.getBlob(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_IMAGE))));

            }
        }


        Button saveBTN = (Button) findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddEditActivity", " Add new Record to DB");
                // TODO: 7/24/2016 add update Record in DB

                name = nameET.getText().toString();
                desc = descET.getText().toString();
                url = null;
//                image=

                if (dbID >= 0) {
                    myMovieDB movie = new myMovieDB(name, desc, url, image);
                    commands.updateDb(movie);
                } else {
                    myMovieDB movie = new myMovieDB(name, desc, url, image);
                    commands.addDb(movie);
                }
                finish();

            }
        });

        Button cancelBTN = (Button) findViewById(R.id.cancelBTN);
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
                EditText urlET = (EditText) findViewById(R.id.urlET);
                String url = urlET.getText().toString();

                DownloadImage loadImage = new DownloadImage(iv, null);

                loadImage.execute(url);

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    /////////////////////////////////// PARSE JSON ///////////////////////////////////////////
    protected void parseJsonForMovieRecord(String result) {

        try {

            //the main JSON object - initialize with string
            JSONObject jsonResult = new JSONObject(result);

            String title = jsonResult.getString("Title");
            String plot = jsonResult.getString("Plot");
            String poster = jsonResult.getString("Poster");
            DownloadImage getImageTask = new DownloadImage(iv,"");
            getImageTask.execute(poster);

//            Bitmap image = imageHelper.getBitmap(poster);
            nameET.setText(title);
            descET.setText(plot);
//            iv.setImageBitmap(image);

            //imageHelper.saveFile(image);


        } catch (JSONException e) {
            e.printStackTrace();

        }

        //  debugTV.setText(result);
    }
    /////////////////////////////////////// END OF PARSE JSON /////////////////////////////////////

    public class DownloadImage extends AsyncTask<String, Integer, Bitmap> {
        ImageView imageView;
        String saveFileLocation;

        public DownloadImage(ImageView imageView, String saveFileLocation) {
            this.imageView = imageView;
            this.saveFileLocation = saveFileLocation;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            myGetImageHelper getImage = new myGetImageHelper();
            Bitmap bmp = getImage.getBitmap(params);
            return bmp;

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressTV.setText("" + values[0]);
            Log.d("Read BMP", "Progress " + values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            myGetImageHelper getImage = new myGetImageHelper();
            image = bmp;
            imageView.setImageBitmap(image);

            getImage.saveFile(bmp);


        }
    }
    ////////////////////////////////////////// Async Task ---- Get Json ///////////////////////////

    class GetJSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            myGetJsonHelper getJson = new myGetJsonHelper();
            String result = getJson.getJsonQuery(uri[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (!result.equals(" {\"Response\":\"False\",\"Error\":\"Movie not found!\"} ")) {
                parseJsonForMovieRecord(result);

            }
        }


    }
}


package clm.mymovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
    TextView debugTV;
    ListView searchLV;
    ArrayList<myMovieQuery> searchResults;
    List<String> searchResultsTitles;
    GetJSONTask getJson;
    EditText searchET;
    String urlQuery;
    myGetImageHelper imageHelper;

    ///////////////////////////////////////////////// onCreate \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d("Search", "Load Activity");

        searchResults= new ArrayList<>();
        searchResultsTitles= new ArrayList<>();

        searchLV=(ListView) findViewById(R.id.searchLV);
        searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this,"Selected"+position,Toast.LENGTH_SHORT);
                String imdbID;
                imdbID=searchResults.get(position).imdbID;
                Intent intent = new Intent(SearchActivity.this,AddEditActivity.class);
                intent.putExtra(myConstants.DB_IMDB,imdbID);
                intent.putExtra(myConstants.DB_ID,-2);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

       /* searchLV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SearchActivity.this,"",Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        searchET = (EditText) findViewById(R.id.searchET);
        searchET.setText("Batman");
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Search", "Query Text Change (ET)");
                loadJsonQuery();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // on itemClick Listener -> load movie data to addEditActivity
/*        searchLV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        // Cancel Button
        Button cancelBTN = (Button) findViewById(R.id.cancelSearchBTN);
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Search", "Cancel Activity");
                finish();
            }
        });
        // Search Button
        Button searchBTN = (Button) findViewById(R.id.searchBTN);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Search", "GetJson For Query");
                loadJsonQuery();
                refreshSearchList();

            }
        });


    }

    ////////////////////////////////////////////////////End onCreate \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void loadJsonQuery() {
        urlQuery = myConstants.OMDB_QUERY_PREFIX + searchET.getText().toString()+"*";
        getJson= new GetJSONTask();
        getJson.execute(urlQuery);


    }

    protected void refreshSearchList() {


        mySearchAdapter adapter
                = new mySearchAdapter( SearchActivity.this ,R.id.searchMovieNameTV,searchResults);
        adapter.notifyDataSetChanged();
        searchLV.setAdapter(adapter);



    }
    /////////////////////////////////// PARSE JSON ///////////////////////////////////////////
    protected ArrayList<myMovieQuery> parseJsonForMovieQuery(String result) {
        ArrayList<myMovieQuery> queryList=new ArrayList<>();

        try {

            //the main JSON object - initialize with string
            JSONObject jsonResult = new JSONObject(result);

            //extract data with getString, getInt getJsonObject - for inner objects or JSONArray- for inner arrays
            JSONArray myArray = jsonResult.getJSONArray("Search");

            for (int i = 0; i < myArray.length(); i++) {
                //get temp inner object [i] inside the array
                JSONObject tempObj = myArray.getJSONObject(i);
                // parse the inside of the object to a new myMovieQuery
                // todo create movie
                imageHelper=new myGetImageHelper();
                imageHelper.execute(tempObj.getString("Poster"));
                // TODO: 7/27/2016  if poster is null 
                Bitmap bmp=   imageHelper.get();
               // Log.d("ParseJson","imageHelper.get :"+bmp.toString());
                myMovieQuery movie= new myMovieQuery(
                            tempObj.getString("Title"),
                            tempObj.getString("Year"),
                            tempObj.getString("imdbID"),
                            tempObj.getString("Type"),
                            tempObj.getString("Poster"),
                            bmp);
                Log.d("ParseJson","imageHelper.get :"+imageHelper.toString());
                queryList.add(movie);


            }



        } catch (JSONException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //  debugTV.setText(result);
        return queryList;
    }
    /////////////////////////////////////// END OF PARSE JSON /////////////////////////////////////



    ////////////////////////////////// GetJSONTask ////////////////////////////////////////////////////
    class GetJSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            myGetJsonHelper getJson=new myGetJsonHelper();
            String result = getJson.getJsonQuery(uri[0]);

            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        /*    StringBuilder stringBuild = new StringBuilder();
            stringBuild.append();//{"Response":"False","Error":"Movie not found!"}
          */
            if (!result.equals(" {\"Response\":\"False\",\"Error\":\"Movie not found!\"} ")) {
                searchResults = parseJsonForMovieQuery(result);

            }
            refreshSearchList();
        }


    }
}
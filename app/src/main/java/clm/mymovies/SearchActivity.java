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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
    TextView searchMessageTV;
    ListView searchLV;
    ArrayList<myMovieQuery> searchResults;
    List<String> searchResultsTitles;
    GetJSONTask getJson;
    EditText searchET;
    String urlQuery;
    myGetImageHelper imageHelper;
    String JsonQueryResult;


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
        JsonQueryResult="";

        searchMessageTV = (TextView) findViewById(R.id.searchMessageTV);

        searchET = (EditText) findViewById(R.id.searchET);
        searchET.setText("Batman");
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Search", "Query Text Change (ET)");
                searchLV.setVisibility(View.INVISIBLE);
                searchMessageTV.setText("Searching..");
                searchMessageTV.setVisibility(View.VISIBLE);
                loadJsonQuery();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        String query = null;
        try {
            query = URLEncoder.encode(searchET.getText().toString()+"*", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        urlQuery = myConstants.OMDB_QUERY_PREFIX +query;
        getJson= new GetJSONTask();
        getJson.execute(urlQuery);


    }

    protected void refreshSearchList() {


        mySearchAdapter adapter
                = new mySearchAdapter( SearchActivity.this ,R.id.searchMovieNameTV,searchResults);
        adapter.notifyDataSetChanged();
        searchLV.setAdapter(adapter);
        if (adapter.getCount()>0){
            searchMessageTV.setVisibility(View.INVISIBLE);
        }
        else {
            searchMessageTV.setVisibility(View.VISIBLE);

        }



    }
    /////////////////////////////////// PARSE JSON ///////////////////////////////////////////
    protected ArrayList<myMovieQuery> parseJsonForMovieQuery(String result) {
        ArrayList<myMovieQuery> queryList=new ArrayList<>();

        try {

            //the main JSON object - initialize with string
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.getBoolean("Response")==true) {
                //extract data with getString, getInt getJsonObject - for inner objects or JSONArray- for inner arrays

                Log.d("ParseJson","Loading Results! ");
                searchMessageTV.setText("Loading Results..");

                JSONArray myArray = jsonResult.getJSONArray("Search");

                for (int i = 0; i < myArray.length(); i++) {
                    //get temp inner object [i] inside the array
                    JSONObject tempObj = myArray.getJSONObject(i);
                    // parse the inside of the object to a new myMovieQuery
                    // todo create movie
                    imageHelper = new myGetImageHelper();
                    imageHelper.execute(tempObj.getString("Poster"));
                    // TODO: 7/27/2016  if poster is null
                    Bitmap bmp = imageHelper.get();
                    // Log.d("ParseJson","imageHelper.get :"+bmp.toString());
                    myMovieQuery movie = new myMovieQuery(
                            tempObj.getString("Title"),
                            tempObj.getString("Year"),
                            tempObj.getString("imdbID"),
                            tempObj.getString("Type"),
                            tempObj.getString("Poster"),
                            bmp);
                    Log.d("ParseJson", "imageHelper.get :" + imageHelper.toString());
                    queryList.add(movie);
                }
                Log.d("ParseJson","Results "+queryList.size());

            } else
                {
                    String errorMessage=jsonResult.getString("Error");
                    Log.d("ParseJson","Error: "+errorMessage);
                    searchMessageTV.setText(errorMessage);
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
                Log.d("GetJsonTask"," Progress Update"+values[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JsonQueryResult=result;

            if (!result.equals(" {\"Response\":\"False\",\"Error\":\"Movie not found!\"} ")) {
                searchResults = parseJsonForMovieQuery(result);

            }
            searchLV.setVisibility(View.VISIBLE);
            refreshSearchList();
        }


    }
}
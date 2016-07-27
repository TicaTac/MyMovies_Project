package clm.mymovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {
    TextView debugTV;
    ListView searchLV;
    ArrayList<myMovieQuery> searchResults;
    List<String> searchResultsTitles;
    GetJSONTask getJson;
    EditText searchET;
    String urlQuery;

    ///////////////////////////////////////////////// onCreate \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getJson = new GetJSONTask();
        debugTV = (TextView) findViewById(R.id.debugTV);
        Log.d("Search", "Load Activity");

        searchResults= new ArrayList<>();
        searchResultsTitles= new ArrayList<>();

        searchLV=(ListView) findViewById(R.id.searchLV);

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

            }
        });


    }

    ////////////////////////////////////////////////////End onCreate \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    protected void refreshSearchList() {


        mySearchAdapter adapter
                = new mySearchAdapter( SearchActivity.this ,R.id.searchMovieNameTV,searchResults);

        searchLV.setAdapter(adapter);

    }
    /////////////////////////////////// PARSE JSON ///////////////////////////////////////////
    protected ArrayList<myMovieQuery> parseJson(String result) {
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

                myMovieQuery movie= new myMovieQuery(
                            tempObj.getString("Title"),
                            tempObj.getInt("Year"),
                            tempObj.getString("imdbID"),
                            tempObj.getString("Type"),
                            tempObj.getString("Poster") );
                queryList.add(movie);

            }



        } catch (JSONException e) {
            e.printStackTrace();

        }

         //  debugTV.setText(result);
        return queryList;
    }
    /////////////////////////////////////// END OF PARSE JSON /////////////////////////////////////

    public void loadJsonQuery() {
        urlQuery = myConstants.OMDB_QUERY_PREFIX + searchET.getText().toString();
        getJson.execute(urlQuery);
        //   refreshSearchList();
    }

    ////////////////////////////////// GetJSONTask ////////////////////////////////////////////////////
    class GetJSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {

            BufferedReader input = null;
            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = null;
            URL url;
            int lineCount = 0;
            try {
                url = new URL(uri[0]);

                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    connection.getResponseCode();
                    input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    //go over the input, line by line
                    String line = "";
                    while ((line = input.readLine()) != null) {
                        //append it to a StringBuilder to hold the
                        //resulting string
                        response.append(line + "\n");
                        //   lineCount++;
                    }
                } else {
                    // See documentation for more info on response handling
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (input != null) {
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    //must disconnect the connection
                    connection.disconnect();
                }

            }
            return String.valueOf(response);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        /*    StringBuilder stringBuild = new StringBuilder();
            stringBuild.append();//{"Response":"False","Error":"Movie not found!"}
          */
            if (!result.equals(" {\"Response\":\"False\",\"Error\":\"Movie not found!\"} ")) {
                searchResults = parseJson(result);
                refreshSearchList();
            }
        }


    }
}
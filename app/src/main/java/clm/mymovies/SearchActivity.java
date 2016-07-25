package clm.mymovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    List searchResults;
    RequestTask getJson;
    TextView urlTV;
    String urlQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        debugTV=(TextView) findViewById(R.id.debugTV);
        searchLV=(ListView) findViewById(R.id.searchLV);
        urlTV=(TextView) findViewById(R.id.urlTV);
        urlQuery=urlTV.getText().toString();
        searchResults=new ArrayList<>();

        getJson = new RequestTask();



        refreshSearchList();
        // on itemClick Listener -> load movie data to addEditActivity
        searchLV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button searchBTN= (Button) findViewById(R.id.searchBTN);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get search list from EditText
                urlQuery=urlTV.getText().toString();

                getJson.execute(urlQuery);

            }
        });


    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {

            BufferedReader input = null;
            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = null;
            URL url;
            int lineCount=0;
            try {
                url = new URL(uri[0]);

                connection = (HttpURLConnection) url.openConnection();
                if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                    connection.getResponseCode();
                    input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    //go over the input, line by line
                    String line="";
                    while ((line=input.readLine())!=null){
                        //append it to a StringBuilder to hold the
                        //resulting string
                        response.append(line+"\n");
                     //   lineCount++;
                    }
                }
                else {
                   // See documentation for more info on response handling
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }

            }
            return String.valueOf(response);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..

            try {

                //the main JSON object - initialize with string
                JSONObject jsonObject= new JSONObject(result);

                //extract data with getString, getInt getJsonObject - for inner objects or JSONArray- for inner arrays
                String name= jsonObject.getString("Search");
                JSONArray myArray= jsonObject.getJSONArray("Title");
                Log.d("json", name);

                for(int i=0; i<myArray.length(); i++)
                {
                    //inner objects inside the array
                    JSONObject innerObj= myArray.getJSONObject(i);
                    String description= innerObj.getString("description");
                    Log.d("json", description);
                }

                JSONObject tempObject=   jsonObject.getJSONObject("main");
                double  temp=   tempObject.getDouble("temp");
                Log.d("json", ""+temp);

            } catch (JSONException e) {
                e.printStackTrace();

            }

            debugTV.setText(result);
        }

    }

    protected void refreshSearchList()
    {
        String[] from={};
        int[] to={};
        SimpleAdapter adapter
                = new SimpleAdapter( SearchActivity.this,searchResults,R.layout.single_movie_search_item,from,to);

        searchLV.setAdapter(adapter);
    }
}

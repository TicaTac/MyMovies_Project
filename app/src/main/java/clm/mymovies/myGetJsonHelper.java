package clm.mymovies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by CLM on 7/27/2016.
 */
public class myGetJsonHelper {

    public String getJsonQuery(String uri)
    {
        BufferedReader input = null;
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;
        URL url;
        int lineCount = 0;
        try {
            url = new URL(uri);

            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
              //  connection.getResponseCode();
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
}

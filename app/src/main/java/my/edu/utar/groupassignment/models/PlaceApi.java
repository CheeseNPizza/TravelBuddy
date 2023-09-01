package my.edu.utar.groupassignment.models;

import my.edu.utar.groupassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {

    /**
     * Retrieves a list of autocomplete suggestions for a given input query.
     *
     * @param input The user's input query.
     * @return An ArrayList of autocomplete suggestions.
     * @throws MalformedURLException If the URL for API request is malformed.
     */

    public ArrayList<String> autoComplete(String input) throws MalformedURLException {
        // Initialize an ArrayList to store autocomplete results
        ArrayList<String> arrayList = new ArrayList();
        HttpURLConnection connection = null;
        // A StringBuilder to store the JSON response
        StringBuilder jsonResult = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            sb.append("input=" + input);
            sb.append("&key=AIzaSyBQHvm5Yp43P5c2L4dnAFh8Myhp5R2H9mM");
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff = new char[1024];
            while((read=inputStreamReader.read(buff))!=-1)
            {
                jsonResult.append(buff,0,read);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null){
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray prediction = jsonObject.getJSONArray("predictions");

            // Extract "description" field from each prediction and add to the arrayList
            for (int i = 0; i < prediction.length(); i++) {
                arrayList.add(prediction.getJSONObject(i).getString("description"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the list of autocomplete suggestions
        return arrayList;
    }
}

package edu.cuhackit.breadcrumbs;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoryModel extends AndroidViewModel {

    public static final String serverAddr = "http://34.203.71.55:11664/";

    private static MutableLiveData<List<StoryClass>> storyList;

    public StoryModel(@NonNull Application application) {
        super(application);
    }

    public static LiveData<List<StoryClass>> getStoryList(double lat, double lng){
        storyList = new MutableLiveData<>();
        loadStories(lat, lng);

        return storyList;
    }

    public static void loadStories(double lat, double lng){
        Double[] inputArr = new Double[2];
        inputArr[0] = lat;
        inputArr[1] = lng;

        new QueryStories().execute(inputArr);
    }

    public static class QueryStories extends AsyncTask<Double, Void, List<StoryClass>>{

        @Override
        protected List<StoryClass> doInBackground(Double... doubles) {
            //Pulls the metadata for the events
            List<StoryClass> response;
            try {
                URL queryUrl = new URL(serverAddr
                        + "metadata"
                        + "?lat=" + doubles[0]
                        + "&lng=" + doubles[1]);
                HttpURLConnection serverConnection = (HttpURLConnection) queryUrl.openConnection();

                try {
                    //variables to read in the data and store it
                    BufferedReader jsonReader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
                    StringBuilder jsonBuilder = new StringBuilder();

                    String eqData;

                    //reads in each line of json data and adds it to the jsonBuilder
                    while ((eqData = jsonReader.readLine()) != null) {
                        jsonBuilder.append(eqData).append('\n');
                    }
                    jsonReader.close();

                    //parses the json string to a list of events
                    response = parseResponse(jsonBuilder.toString());

                } finally {
                    serverConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("EventModel", e.getMessage(), e);
                return null;
            }

            return response;
        }

        protected void onPostExecute(List<StoryClass> finishedList){
            if(finishedList == null) { return; }
            storyList.setValue(finishedList);
        }
    }

    private static List<StoryClass> parseResponse(String json){
        ArrayList<StoryClass> response = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(json);
            JSONArray array = new JSONArray(root.getJSONArray("stories"));
            Log.i("URL", json);

            for(int i = 0; i < array.length(); i++){
                double lat;
                double lng;
                String id;
                String caption;

                JSONObject element = array.getJSONObject(i);
                lat = element.getDouble("lat");
                lng = element.getDouble("lng");
                id = element.getString("_id");
                caption = element.getString("caption");

                response.add(new StoryClass(lat, lng, id, caption));
            }

        } catch (JSONException e){

        }

        for (int i = 0; i < response.size(); i++) {
            try {
                //creates a separate connection to the server asking for each image
                URL queryUrl = new URL(serverAddr + "storyImage" + "?id=" + response.get(i).getId());
                HttpURLConnection iHConnection = (HttpURLConnection) queryUrl.openConnection();
                Bitmap bmpResponse = null;

                //read the response and decode the image
                try {
                    bmpResponse = BitmapFactory.decodeStream(iHConnection.getInputStream());
                } finally {
                    //bind the parsed image to the corresponding EventClass
                    if(bmpResponse != null && response.get(i) != null) response.get(i).setImg(bmpResponse);
                    iHConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("EventModel", e.getMessage(), e);
                return null;
            }
        }

        return response;
    }
}

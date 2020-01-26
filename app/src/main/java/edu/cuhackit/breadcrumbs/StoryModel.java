package edu.cuhackit.breadcrumbs;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarReceiver;
import io.radar.sdk.model.Coordinate;
import io.radar.sdk.model.RadarCircleGeometry;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;

public class StoryModel extends AndroidViewModel {

    public static final String serverAddr = "http://34.203.71.55:11664/";

    private static LiveData<List<StoryClass>> storyList;

    private static LiveData<Double[]> currentCoordinates;

    private static LiveData<ArrayList<Coordinate>> radarCoords;

    public StoryModel(@NonNull Application application) {
        super(application);
    }

    private static List<StoryClass> parseResponse(String json){
        ArrayList<StoryClass> response = null;

        try{
            JSONObject root = new JSONObject(json);
            JSONArray array = new JSONArray(root.getJSONArray("stories"));

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
                    if(bmpResponse != null && response.get(i) != null) response.get(i).setBmp(bmpResponse);
                    iHConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("EventModel", e.getMessage(), e);
                return null;
            }

        }
        return eventResponse;

        return response;
    }
}

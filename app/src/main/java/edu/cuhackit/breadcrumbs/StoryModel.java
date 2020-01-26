package edu.cuhackit.breadcrumbs;

import android.app.Application;
import android.content.Context;
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

    private class MyRadarReceiver extends RadarReceiver{

        final static String TAG = "RadarReceiver";

        private LiveData<ArrayList<Coordinate>> outputCoords;

        @Override
        public void onError(@NotNull Context context, @NotNull Radar.RadarStatus radarStatus) {
            Log.i(TAG, "onError");
        }

        @Override
        public void onLocationUpdated(@NotNull Context context, Location location, RadarUser user) {
            Log.i(TAG, "onLocationUpdated: " + location.getLatitude() + " " + location.getLongitude());
            Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onEventsReceived(@NotNull Context context, @NotNull RadarEvent[] radarEvents, @NotNull RadarUser radarUser) {
       /*
            Test cases:
            - enter geofence , exit into non-geofence
                - result: length 1
            - enter geofence, exit into another geo-fence
                - result: length 2
         */

            Log.i(TAG, "onEventsReceived: " +
                    "Length: " + radarEvents.length);
            Toast.makeText(context, "Event Triggered", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "Event Triggered", Toast.LENGTH_SHORT).show();

            ArrayList<Coordinate> coordList = new ArrayList<>();

            for(int i = 0; i < radarEvents.length; i++){ //loop through events
                //if event is User Entering Geofence
                if(radarEvents[i].getType() == RadarEvent.RadarEventType.USER_ENTERED_GEOFENCE){
                    //get the coordinates
                    RadarCircleGeometry geoshape = (RadarCircleGeometry) radarEvents[0].getGeofence().getGeometry();
                    Coordinate center = geoshape.getCenter();
                    coordList.add(center);
                    MutableLiveData<ArrayList<Coordinate>> temp = new MutableLiveData<>();
                    temp.postValue(coordList);
                    radarCoords = temp;
                }
            }
            Log.i(TAG, "coordList length: " + coordList.size());
        }

    }

    public static class QueryStoryMeta extends AsyncTask<Double, Void, MutableLiveData>{

        @Override
        protected MutableLiveData doInBackground(Double... coords) {
            List<StoryClass> result = null;

            try{
                URL url = new URL(serverAddr + "storyMetaData");
                HttpURLConnection serverConnection = (HttpURLConnection) url.openConnection();

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
                    result = parseResponse(jsonBuilder.toString());
                } finally {
                    serverConnection.disconnect();
                }

            } catch(Exception e){
                return null;
            }

            MutableLiveData output = new MutableLiveData<List<StoryClass>>();
            output.postValue(result);

            return output;
        }
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

        return response;
    }
}

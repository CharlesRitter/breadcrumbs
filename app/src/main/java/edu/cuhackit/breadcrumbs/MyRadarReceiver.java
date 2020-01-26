package edu.cuhackit.breadcrumbs;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarReceiver;
import io.radar.sdk.model.Coordinate;
import io.radar.sdk.model.RadarCircleGeometry;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;

public class MyRadarReceiver extends RadarReceiver {

    final static String TAG = "RadarReceiver";



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

        ArrayList<LatLng> coordList = new ArrayList<>();

        for (int i = 0; i < radarEvents.length; i++) { //loop through events
            //if event is User Entering Geofence
            if (radarEvents[i].getType() == RadarEvent.RadarEventType.USER_ENTERED_GEOFENCE) {
                //get the coordinates
                RadarCircleGeometry geoshape = (RadarCircleGeometry) radarEvents[i].getGeofence().getGeometry();
                Coordinate center = geoshape.getCenter();
                LatLng coord = new LatLng(center.getLatitude(), center.getLongitude());
                coordList.add(coord);
            }
        }
        Log.i(TAG, "coordList length: " + coordList.size());

        //https://stackoverflow.com/questions/38954261/how-to-pass-data-from-broadcastreceiver-to-activity-without-in-oncreate
        Intent i = new Intent("mycustombroadcast");
        //i.putExtra("phone_num", coordList.size());
        i.putParcelableArrayListExtra("list", coordList);
        context.sendBroadcast(i);
    }

}

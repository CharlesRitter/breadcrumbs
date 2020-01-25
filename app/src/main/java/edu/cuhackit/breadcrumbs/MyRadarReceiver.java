package edu.cuhackit.breadcrumbs;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarReceiver;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarGeofence;
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
        Log.i(TAG, "onEventsReceived: " +
                "Length: " + radarEvents.length);
        Toast.makeText(context, "Event Triggered", Toast.LENGTH_SHORT).show();
    }
}

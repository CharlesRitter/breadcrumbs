package edu.cuhackit.breadcrumbs;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarReceiver;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;

public class MyRadarReceiver extends RadarReceiver {

    @Override
    public void onError(@NotNull Context context, @NotNull Radar.RadarStatus radarStatus) {

    }

    @Override
    public void onLocationUpdated(@NotNull Context context, Location location, RadarUser user) {

    }

    @Override
    public void onEventsReceived(@NotNull Context context, @NotNull RadarEvent[] radarEvents, @NotNull RadarUser radarUser) {
        Toast.makeText(context, "I got something", Toast.LENGTH_SHORT).show();
        Log.i("Something", radarEvents[0].toString());

    }
}

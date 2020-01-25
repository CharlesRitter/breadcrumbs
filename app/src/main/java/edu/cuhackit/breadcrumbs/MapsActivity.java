package edu.cuhackit.breadcrumbs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import io.radar.sdk.Radar;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarGeofence;
import io.radar.sdk.model.RadarUser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final static String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i(TAG, "Tracking: " + Radar.isTracking());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Radar.initialize("prj_test_pk_b2451fcc967db99f2912c986d8c75cea785ca5d0"); //Jemiah's key
        Radar.initialize("prj_test_pk_668bab55b5fbac2e7a4a28247c3d57ccfb5160e3"); //Nikita's key

        Radar.trackOnce(new Radar.RadarCallback() {
            @Override
            public void onComplete(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {

                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,
                        20));

            }
        });

        Radar.startTracking();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        Radar.stopTracking();
    }
}

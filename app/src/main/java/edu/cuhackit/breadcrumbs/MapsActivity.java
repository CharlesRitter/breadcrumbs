package edu.cuhackit.breadcrumbs;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import io.radar.sdk.Radar;
import io.radar.sdk.model.Coordinate;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarGeofence;
import io.radar.sdk.model.RadarUser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import android.content.Context;

import java.util.ArrayList;
import java.util.Observer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    final static String TAG = "MapsActivity";

    private BroadcastReceiver broadcastReceiver;
    private ArrayList<LatLng> coordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = new Intent(getApplicationContext(), Form.class);
        startActivity(intent);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.i(TAG, "Tracking: " + Radar.isTracking());*/
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

        Radar.initialize("prj_test_pk_b2451fcc967db99f2912c986d8c75cea785ca5d0"); //Jemiah's key
        //Radar.initialize("prj_test_pk_668bab55b5fbac2e7a4a28247c3d57ccfb5160e3"); //Nikita's key
        
        //initial position
        Radar.trackOnce(new Radar.RadarCallback() {
            @Override
            public void onComplete(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {

                try {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("Start")
                            .snippet("The Beginning")
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ball)));
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,
                            20));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLocation)
                            .tilt(60)
                            .zoom(20)
                            .bearing(0)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } catch(Exception e){
                    LatLng currentLocation = new LatLng(0, 0);

                    mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("Start")
                            .snippet("The Beginning")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,
                            20));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLocation)
                            .tilt(60)
                            .zoom(20)
                            .bearing(0)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

            }
        });

        Radar.startTracking();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Bundle bundle = intent.getExtras();
                coordList = intent.getParcelableArrayListExtra("list");
                Log.i(TAG, "list size: " + coordList.size());

                for(LatLng coordinate: coordList){
                    mMap.addMarker(new MarkerOptions().position(coordinate).title("Watt"));
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("mycustombroadcast"));

        //LatLng geofenceCenter = new LatLng(34.67600673480939, -82.83691123558198);
        //mMap.addMarker(new MarkerOptions().position(geofenceCenter).title("Watt"));
        LocationManager locaMana = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locaList = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    mMap.clear();

                    LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions()
                            .position(newLocation)
                            .title("You Are Here")
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ball)));
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,
                            20));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(newLocation)
                            .tilt(60)
                            .zoom(20)
                            .bearing(0)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } catch(Exception e){
                    Log.i("Error", e.toString());
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else{
            locaMana.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locaList);
        }

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        Radar.stopTracking();
        unregisterReceiver(broadcastReceiver);
    }
}

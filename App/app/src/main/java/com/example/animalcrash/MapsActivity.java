package com.example.animalcrash;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.animalcrash.controller.MapController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.util.Map;
import com.example.animalcrash.util.MapUtil;
import com.example.animalcrash.util.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author Robert Neijmeijer
 * Doel Handle and do evertything with the map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, TimePickerDialog.OnTimeSetListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final float MAX_CATCH_DISTANCE = 15.f; // in meters
    private final float ZOOM_LEVEL = 19.f; // max 21


    public Marker currLocationMarker;
    public GoogleApiClient GoogleApiClient;
    private Location lastLocation = new Location(Context.LOCATION_SERVICE);
    private LocationRequest locationRequest = new LocationRequest().create();
    private LocationManager locationManager;
    private GoogleMap map;
    private long timePicked = 90000;

    private MapUtil mapUtil;
    private ArrayList<Map> animalList = new ArrayList<>();
    private MapController mapController;
    private SpelerController spelerController = new SpelerController();
    private SessionManager sessionManager;
    private Marker avatar;
    private boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize controller & session manager
        this.sessionManager = new SessionManager(this);
        this.mapController = new MapController();
        this.mapUtil = new MapUtil(this);

        admin = sessionManager.getBooleanValue("admin");

        // Empty the array list before inserting new data
        animalList.clear();

        // Depending on user role, redirect user to view
        if (admin) {
            setContentView(R.layout.activity_admin_maps);
        } else {
            setContentView(R.layout.activity_maps);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mapUtil.checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        locationRequest.setInterval(1000);
    }

    public float getMAX_CATCH_DISTANCE() {
        return MAX_CATCH_DISTANCE;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        // If user is admin
        if (admin) {
            // Fetch placed animals
            if (mapController.fetchGeplaatsteDieren()) {
                animalList = mapController.getGeplaatsteDieren();

                // For every animal, set the marker
                for (Map placedAnimal : animalList) {
                    placedAnimal.setGoogleMap(map);
                }
            }
        } else {
            // Get current logged in user
            Speler loggedInUser = spelerController.getSpeler(spelerController.getSpelers(),
                    sessionManager.getStringValue("username"));

            if (mapController.fetchNogNietGevangenDieren(loggedInUser.getId())) {
                animalList = mapController.getGeplaatsteDieren();

                // For every animal, set the marker
                for (Map placedAnimal : animalList) {
                    placedAnimal.setGoogleMap(map);
                }
            }
        }
        // Set the UI values
        mapUtil.setMapUI();

        mapUtil.applyCustomMap(googleMap);
        mapUtil.addMapOverlay(map);

        LocationManager mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // If we don't have access to gps show the popup
        if (!enabled) {
            mapUtil.showDialogGPS();
        }

        mapUtil.placeAnimals(this);
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mapUtil.checkLocationPermission()) {
        }

        // Enable the blue dot on the map
        map.setMyLocationEnabled(true);

        //Add the custom avatar on our location
        avatar = map.addMarker( new MarkerOptions()
                        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_rezised)));

        // Move the camera to our position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), ZOOM_LEVEL));

    }

    /**
     * Build the google api client
     */
    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        GoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {
        mapUtil.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (GoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    // Een methode die je zal sturen naar de profielpagina
    public void goToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // Een methode die je zal sturen naar de inventorypagina
    public void goToInventory(View view) {
        Intent intent = new Intent(this, RecyclerView.class);
        startActivity(intent);
    }

    public void goScoreBoard(View view) {
        Intent intent = new Intent(this, ScoreBoardActivity.class);
        startActivity(intent);
    }

    /**
     * Redirect to admin dashboard view
     *
     * @param view View
     */
    public void goToAdminDashboard(View view) {
        MainActivity.getInstance().goToAdminDashboard();
    }

    @Override
    /**
     * Gets called when the user clicked on a marker
     * @param marker the marker the user clicked on
     */
    public boolean onMarkerClick(Marker marker) {
        mapUtil.onMarkerClick(marker);
        return true;
    }

    @Override
    /**
     * Gets called when the user picked a time
     * @param hourOfDay the hour picked
     * @param minite the minute picked
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Date date = new Date();
        date.setHours(hourOfDay);
        date.setMinutes(minute);
        timePicked = date.getTime();
    }

    public MapsActivity getInstacne(){
        return this;
    }

    public void startActivities(Intent intent){
        startActivity(intent);
    }

    public GoogleMap getMap(){return map;}

    public boolean getAdmin(){return admin;}

    @Override
    /**
     * Gets called when the user cklicked on the map
     */
    public void onMapClick(LatLng latLng) {
        mapUtil.onMapClick(latLng);
    }

    /**
     * Sets the position of the avatar to the position given
     * @param latLng The position the avatar is set to (The current location)
     */
    public void setAvatarPosition(LatLng latLng){avatar.setPosition(latLng);}

    public void setLastLocation(Location location){lastLocation = location;}

    public ArrayList<Map> getAnimalList(){return animalList;}
    public void setAnimalList(LatLng latLng,int id){ animalList.add(new Map(latLng,id, map));}
}
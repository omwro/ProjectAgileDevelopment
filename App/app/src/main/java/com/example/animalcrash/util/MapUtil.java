package com.example.animalcrash.util;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.animalcrash.MapsActivity;
import com.example.animalcrash.R;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.MedewerkerController;
import com.example.animalcrash.model.Dier;
import com.example.animalcrash.model.Medewerker;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.animalcrash.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.example.animalcrash.R.style.AlertDialogTheme;

/**
 * @author Robert Neijmeijer
 */
public class MapUtil {

    private final int MIN_ANIMAL_VALUE = 10;
    private final int MAX_ANIMAL_VALUE = 91;

    private MapsActivity mapsActivity;
    private SessionManager sessionManager;
    private DierController dierController = new DierController();
    private MedewerkerController medewerkerController = new MedewerkerController();

    private final LatLng amsta1 = new LatLng(52.364115, 4.908643);
    private final LatLng amsta2 = new LatLng(52.364928, 4.910486);
    private final LatLng ksh1 = new LatLng(52.358492, 4.908738);
    private final LatLng ksh2 = new LatLng(52.359472, 4.908706);

    private Calendar today = Calendar.getInstance();

    public MapUtil(MapsActivity mapsActivity){
        this.mapsActivity = mapsActivity;
        this.sessionManager = new SessionManager(mapsActivity);
    }

    /**
     *
     * @param marker The marker to check the distance too
     * @param mLastLocation The current last known location
     * @return
     */
    public boolean distanceCheck(Marker marker, Location mLastLocation ) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(marker.getPosition().latitude);
        location.setLongitude(marker.getPosition().longitude);

        float distance = mLastLocation.distanceTo(location);

        return distance <= mapsActivity.getMAX_CATCH_DISTANCE();
    }
    /**
     * When called prompts the user to share location data with the app, gets called on startup of mapsactivity
     */
    public void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mapsActivity.getInstacne(), AlertDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mapsActivity.startActivity(intent);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * Gets called when the user clickes on the map
     * @param latLng the latLng of the position the user clicked
     */
    public void onMapClick(LatLng latLng){
        final LatLng lat = latLng;
        // If user is admin
        if (sessionManager.getBooleanValue("admin")) {
            ArrayList<Dier> dieren = new DierController().getDieren();
            String[] alleDieren = new String[dieren.size()];

            for (int i = 0; i < dieren.size(); i++) {
                alleDieren[i] = dieren.get(i).getNaam();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(mapsActivity.getInstacne());
            builder.setTitle("Selecteer een dier om te plaatsen");
            builder.setItems(alleDieren, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //timeDialog();
                    mapsActivity.setAnimalList(lat,which); // Add placed animal into the array list
                    placeNewAnimal((which + 1), lat); // Insert animal into the database
                    placeAnimals(mapsActivity); // Place animal on the map
                }

            });
            builder.show();
        }

    }

    /**
     * Gets called when the user clicks on a marker
     * @param marker the marker that the user clicked on
     */
    public void onMarkerClick(Marker marker){
        Log.d("CLICK", "Clicked on a marker");

        for (Map placedAnimal : mapsActivity.getAnimalList()) {
            if (marker.getPosition().equals(placedAnimal.getLocation())) {

                // If clicked user is not admin, continue
                if (!sessionManager.getBooleanValue("admin")) {
                    if (distanceCheck(marker, mapsActivity.getLastLocation())) {
                        Log.d("CLICK", "Marker ID:" + placedAnimal.getId() + " / Catching animal ID:" + placedAnimal.getAnimalId());
                        placedAnimal.catchAnimal(placedAnimal.getId(), placedAnimal.getAnimalId(), marker, mapsActivity);
                    } else {
                        DierController dierController = new DierController();
                        Dier dier = dierController.getDierById(placedAnimal.getAnimalId());

                        Toast.makeText(mapsActivity, "De " + dier.getNaam() + " is te ver weg, zorg dat je dichter bij het dier staat",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * Create and insert new animal on the map
     *
     * @param animalId int      id of inserted animal
     * @param location LatLng   location of inserted animal
     */
    private void placeNewAnimal(int animalId, LatLng location) {
        // Get current logged in employee
        Medewerker employee = medewerkerController.getMedewerker(medewerkerController.getMedewerkers(),
                sessionManager.getStringValue("username"));

        // Get inserted animal object
        Dier animal = dierController.getDierById(animalId);

        // Generate random number between 10 and 100
        int randomizedAnimalValue = MIN_ANIMAL_VALUE + (int) (Math.random() * (MAX_ANIMAL_VALUE));

        // Get and set all the values to be inserted into the database through http request
        HashMap<String, String> data = new HashMap<>();
        data.put("animal", String.valueOf(animalId)); // Animal ID
        data.put("employee", String.valueOf(employee.getId())); // Employee ID
        data.put("posX", String.valueOf(location.latitude)); // Location: latitude
        data.put("posY", String.valueOf(location.longitude)); // Location: longitude
        data.put("value", String.valueOf(randomizedAnimalValue)); // Animal value

        // Setup the http request
        JSONObject httpRequest = new HttpRequest("animals/place", true)
                .postRequest(data);

        Toast.makeText(mapsActivity.getApplicationContext(), "U heeft succesvol een dier geplaatst (" + animal.getNaam() + ")", Toast.LENGTH_SHORT).show();
    }

    /**
     * Places the animal in the animallist
     * @param context
     */
    public void placeAnimals(Context context) {
        for (Map placedAnimal : mapsActivity.getAnimalList()) {
            placedAnimal.placeAnimal(context);
        }
    }

    /**
     * Checks if the date is before the current date
     * @param time The date to check
     * @return Returns true if the date is before, else it returns false
     */
    public boolean timeAfter(long time) {
        Date date = new Date();
        date.setTime(time);
        return today.before(date);
    }

    /**
     * Adds the ground overlay to the map
     * @param mMap the map for the overlay to be placed on
     */
    public void addMapOverlay(GoogleMap mMap) {
        // Set the bounds for the image to be placed on the map
        LatLngBounds AMSTA = new LatLngBounds(
                amsta1,// Top right
                amsta2// Bottom left
        );

        LatLngBounds HVA = new LatLngBounds(
                ksh1,// Top right
                ksh2// Bottom left
        );

        // Make the ground overlay with the image and set the position
        GroundOverlayOptions amstaSap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.amsta_plattegrond_bitmaps_small))
                .positionFromBounds(AMSTA);

        GroundOverlayOptions hva = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.ksh_map))
                .positionFromBounds(HVA);

        //Apply the ground overlay
        mMap.addGroundOverlay(amstaSap);
        //mMap.addGroundOverlay(hva);
        // TODO: get new map of ksh and add it
    }

    /**
     * Applies a custom map instead of the normal google map
     * @param googleMap The map to be applied too
     */
    public void applyCustomMap(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            mapsActivity, R.raw.map_json));

        } catch (Resources.NotFoundException e) {
        }
    }

    public void timeDialog() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(mapsActivity.getSupportFragmentManager(), "time picker");
    }

    /**
     * Checks if the permissions are granded
     * @return true if all permissions are grandted else returns false
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(mapsActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                /* Show an explanation to the user *asynchronously* -- don't block
                 this thread waiting for the user's response! After the user
                 sees the explanation, try again to request the permission. */

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(mapsActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mapsActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets all the UI settings for the map
     */
    public void setMapUI(){
        // Disable the compass
        mapsActivity.getMap().getUiSettings().setCompassEnabled(false);

        // Gestures only enabled for admins
        mapsActivity.getMap().getUiSettings().setScrollGesturesEnabled(mapsActivity.getAdmin());
        mapsActivity.getMap().getUiSettings().setAllGesturesEnabled(mapsActivity.getAdmin());

        mapsActivity.getMap().setOnMapClickListener(mapsActivity.getInstacne());
        mapsActivity.getMap().setOnMarkerClickListener(mapsActivity.getInstacne());

        // Enable the blue dot on the map
        mapsActivity.getMap().getUiSettings().setMyLocationButtonEnabled(true);


    }

    /**
     * Gets called when the current location has changed from the last location
     * @param location the new location of the user
     */
    public void onLocationChanged(Location location){
        if (mapsActivity.currLocationMarker != null) {
            mapsActivity.currLocationMarker.remove();
        }

        // Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //mCurrLocationMarker.setPosition(latLng);

        mapsActivity.setLastLocation(location);
        mapsActivity.setAvatarPosition(latLng);

        // Move map camera and handle zoom
        mapsActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));

        // Stop locatie updates
        if (mapsActivity.GoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(mapsActivity.getInstacne()).flushLocations();
        }
    }
}

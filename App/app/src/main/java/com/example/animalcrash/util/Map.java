package com.example.animalcrash.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.example.animalcrash.R;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Dier;
import com.example.animalcrash.model.Speler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;

public class Map extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private int id;
    private int animalId;
    private int employeeId;
    private LatLng location;
    private int animalValue;
    private GoogleMap googleMap;
    private DierController dierController = new DierController();

    public Map() {
    }

    public Map(LatLng location, int animalId, GoogleMap googleMap) {
        this.location = location;
        this.animalId = animalId + 1; // +1 so the index starts at 1 instead of 0
        this.googleMap = googleMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getAnimalValue() {
        return animalValue;
    }

    public void setAnimalValue(int animalValue) {
        this.animalValue = animalValue;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void placeAnimal(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        if (!sessionManager.getBooleanValue("admin")) {
            SpelerController spelerController = new SpelerController();
            Speler speler = spelerController.getSpeler(spelerController.getSpelers(),
                    sessionManager.getStringValue("username"));

            // Als index ge gevult
            if (speler.getGevangenIndexes().size() > 0) {
                // Voor elke index
                for (int index : speler.getGevangenIndexes()) {
                    // Als index gelijk staat aan marker id
                    if (getId() == index) {
                        // Niet toevoegen
                        Log.d("TEST", getId() + " == " + index + ". Not adding it tot the map");
                        return;
                    }
                }
            }
        }

        // Place animal on the map
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(dierController.getAnimalColouredImage(animalId)))
        );

    }

    /**
     * Hieronder wordt er aangegeven dat een dier is gevangen en wordt met een id doorgegeven.
     *
     * @param id
     */
    public void catchAnimal(int id, int animalId, Marker marker, Context context) {
        // Getting the logged in player
        SpelerController spelerController = new SpelerController();
        SessionManager sessionManager = new SessionManager(context);
        Speler speler = spelerController.getSpeler(spelerController.getSpelers(),
                sessionManager.getStringValue("username"));

        DierController dierController = new DierController();
        Dier caughtAnimal = dierController.getDierById(animalId);

        // Removing the marker
        marker.remove();

        if (!speler.getDieren().contains(caughtAnimal)) {
            speler.addDieren(caughtAnimal);
            speler.addGevangenIndexes(id);

            // adding xp and money to user
            speler.addXP(getAnimalValue());
            speler.addGeld(getAnimalValue());

            // Get animal value (money/xp amount)
            HashMap<String, String> bodyData = new HashMap<>();
            bodyData.put("currency", String.valueOf(speler.getGeld()));
            bodyData.put("XP", String.valueOf(speler.getXP()));

            // Update user money/xp
            HttpRequest httpRequest = new HttpRequest("users/" + speler.getId(), true);
            httpRequest.putRequest(bodyData);

            // Get animal/user details
            HashMap<String, String> bodyData1 = new HashMap<>();
            bodyData1.put("user", String.valueOf(speler.getId()));
            bodyData1.put("animal", String.valueOf(caughtAnimal.getId()));
            bodyData1.put("placed_animal", String.valueOf(getId()));

            // Save caught animal for user
            HttpRequest httpRequest1 = new HttpRequest("animals/catch", true);
            httpRequest1.postRequest(bodyData1);

            // Pop-up message
            StringBuilder message = new StringBuilder();
            message.append("Je hebt succesvol een " + caughtAnimal.getNaam() + " gevangen!\n");
            message.append("\n+" + getAnimalValue() + " XP");
            message.append("\n+" + getAnimalValue() + " GELD");

            // Create the alert
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setTitle("Dier gevangen!")
                    .setMessage(message.toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.ic_animal_paw_print);

            // Display pop-up
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MARKER CLICKED", "A marker was clicked");
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}

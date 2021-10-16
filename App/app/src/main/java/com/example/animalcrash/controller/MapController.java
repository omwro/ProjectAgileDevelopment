package com.example.animalcrash.controller;

import com.example.animalcrash.repository.MapRepository;
import com.example.animalcrash.util.Map;
import com.example.animalcrash.util.HttpRequest;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Placed animals class methods
 *              Interaction between View & Repository
 *
 */

public class MapController {

    private final int OBJECT_MINUS_HTTP_RESPONSE = 2;
    private MapRepository repository;

    /**
     * Initialize controller
     */
    public MapController() {
        this.repository = new MapRepository();
    }

    /**
     * Place animal
     *
     * @param placedAnimal Map
     * @see MapRepository
     */
    public void plaatsDier(Map placedAnimal) {
        repository.add(placedAnimal);
    }

    /**
     * Get placed animal by id
     *
     * @param animalId int
     * @return Map
     */
    public Map getGeplaatsteDierById(int animalId) {
        return repository.getById(repository.list(), animalId);
    }

    /**
     * Get placed animals
     *
     * @see MapRepository
     */
    public ArrayList<Map> getGeplaatsteDieren() {
        return repository.list();
    }

    /**
     * Retrieve all placed animals on the map from database
     * And save them into the repository
     *
     * @see MapRepository
     * @return boolean
     */
    public boolean fetchGeplaatsteDieren() {
        int count = 0;
        HttpRequest getPlacedAnimals = new HttpRequest("maps", true);
        JSONObject placedAnimals = getPlacedAnimals.getRequest(true);

        for (int i = 0; i < placedAnimals.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(placedAnimals.getJSONObject(String.valueOf(i))));

                // Create the placed animal
                Map geplaatsteDier = new Map();
                geplaatsteDier.setId(jsonData.getInt("id"));
                geplaatsteDier.setAnimalId(jsonData.getInt("dier_id"));
                geplaatsteDier.setEmployeeId(jsonData.getInt("medewerker_id"));
                geplaatsteDier.setLocation(new LatLng(jsonData.getDouble("posX"), jsonData.getDouble("posY")));
                geplaatsteDier.setAnimalValue(jsonData.getInt("waarde"));

                // Add placed animal object to repository
                plaatsDier(geplaatsteDier);

                // Count placed animals
                count++;

                // Check whether fetching is done
                if (count == placedAnimals.length() - OBJECT_MINUS_HTTP_RESPONSE) {
                    return true;
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception: Error occured while creating a JSON object - [" + e.getMessage() + "]");
            }
        }

        return false;
    }

    /**
     * Retrieve all placed animals (which are not caught yet by user) on the map from database
     * And save them into the repository
     *
     * @see MapRepository
     * @return boolean
     */
    public boolean fetchNogNietGevangenDieren(int userId) {
        int count = 0;
        HttpRequest getPlacedAnimals = new HttpRequest("maps/" + userId, true);
        JSONObject placedAnimals = getPlacedAnimals.getRequest(true);

        for (int i = 0; i < placedAnimals.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(placedAnimals.getJSONObject(String.valueOf(i))));

                // Create the placed animal
                Map geplaatsteDier = new Map();
                geplaatsteDier.setId(jsonData.getInt("id"));
                geplaatsteDier.setAnimalId(jsonData.getInt("dier_id"));
                geplaatsteDier.setEmployeeId(jsonData.getInt("medewerker_id"));
                geplaatsteDier.setLocation(new LatLng(jsonData.getDouble("posX"), jsonData.getDouble("posY")));
                geplaatsteDier.setAnimalValue(jsonData.getInt("waarde"));

                // Add placed animal object to repository
                plaatsDier(geplaatsteDier);

                // Count placed animals
                count++;

                // Check whether fetching is done
                if (count == placedAnimals.length() - OBJECT_MINUS_HTTP_RESPONSE) {
                    return true;
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception: Error occured while creating a JSON object - [" + e.getMessage() + "]");
            }
        }

        return false;
    }
}

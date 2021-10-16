package com.example.animalcrash.controller;

import com.example.animalcrash.model.Medewerker;
import com.example.animalcrash.repository.MedewerkerRepository;
import com.example.animalcrash.util.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author MEHMET TETIK
 * @version 1.0
 * <p>
 * Purpose      Employee class methods
 * Interaction between View & Repository
 */

public class MedewerkerController {

    private final int OBJECT_MINUS_HTTP_RESPONSE = 2;
    private MedewerkerRepository repository;

    /**
     * Initialize controller
     */
    public MedewerkerController() {
        this.repository = new MedewerkerRepository();
    }

    /**
     * Get medewerker
     *
     * @param medewerkers ArrayList<Medewerker>
     * @param username    String
     * @return Medewerker
     * @see MedewerkerRepository
     */
    public Medewerker getMedewerker(ArrayList<Medewerker> medewerkers, String username) {
        return repository.get(medewerkers, username);
    }

    /**
     * Get medewerker by id
     *
     * @param medewerkers ArrayList<Medewerker>
     * @param userId      int
     * @return Medewerker
     */
    public Medewerker getMedewerkerById(ArrayList<Medewerker> medewerkers, int userId) {
        return repository.getById(medewerkers, userId);
    }

    /**
     * Get medewerkers
     *
     * @see MedewerkerRepository
     */
    public ArrayList<Medewerker> getMedewerkers() {
        return repository.list();
    }

    /**
     * Retrieve all medewerkers from database
     * And save them into the repository
     *
     * @see MedewerkerRepository
     * @return boolean
     */

    /**
     * Add speler (on register)
     *
     * @param username String   Username
     * @param gender   String     Gender
     * @param email    String     Email
     * @return reponse JSONObject
     * @see MedewerkerRepository
     */
    public JSONObject addMedewerker(String username, String gender, String email) {
        // Initialize
        HttpRequest httpRequest = new HttpRequest("employees", true);
        HashMap<String, String> data = new HashMap<>();

        // Set the required data
        data.put("username", username);
        data.put("gender", gender);
        data.put("email", email);

        // Post the data
        JSONObject response = httpRequest.postRequest(data);

        try {
            // If it has a response
            if (response.isNull("code")) {
                Medewerker medewerker = new Medewerker();
                medewerker.setId(response.getInt("id"));
                medewerker.setGebruikersnaam(response.getString("gebruikersnaam"));
                medewerker.setGeslacht(response.getString("geslacht"));
                medewerker.setEmail(response.getString("email"));

                // Add the mederwerker to the repository
                repository.add(medewerker);
            } else {
                return response;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean fetchMedewerkers() {
        int count = 0;
        HttpRequest getEmployees = new HttpRequest("employees", true);
        JSONObject employees = getEmployees.getRequest(true);

        for (int i = 0; i < employees.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(employees.getJSONObject(String.valueOf(i))));

                // Create the employee
                Medewerker medewerker = new Medewerker();
                medewerker.setId(jsonData.getInt("id"));
                medewerker.setGebruikersnaam(jsonData.getString("gebruikersnaam"));
                medewerker.setGeslacht(jsonData.getString("geslacht"));
                medewerker.setEmail(jsonData.getString("email"));

                // Add employee object to repository
                repository.add(medewerker);

                // Count employees
                count++;

                // Check whether fetching is done
                if (count == employees.length() - OBJECT_MINUS_HTTP_RESPONSE) {
                    return true;
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception: " + e.getMessage());
            }
        }

        return false;
    }
}

package com.example.animalcrash.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.animalcrash.model.Dier;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.repository.SpelerRepository;
import com.example.animalcrash.util.HttpRequest;
import com.example.animalcrash.util.SpelerComparator;
import com.example.animalcrash.util.TranslateHttpResponse;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Player class methods
 *              Interaction between View & Repository
 *
 */

public class SpelerController {

    private final int OBJECT_MINUS_HTTP_RESPONSE = 2;
    private SpelerRepository repository;

    /**
     * Initialize controller
     */
    public SpelerController() {
        this.repository = new SpelerRepository();
    }

    /**
     * Get speler
     *
     * @see SpelerRepository
     * @param spelers ArrayList<Speler>
     * @param username String
     * @return Speler
     */
    public Speler getSpeler(ArrayList<Speler> spelers, String username) {
        return repository.get(spelers, username);
    }

    /**
     * Get speler by id
     *
     * @param spelers ArrayList<Speler>
     * @param userId int
     * @return Speler
     */
    public Speler getSpelerById(ArrayList<Speler> spelers, int userId) {
        return repository.getById(spelers, userId);
    }

    /**
     * Get spelers
     *
     * @see SpelerRepository
     */
    public ArrayList<Speler> getSpelers() {
        return repository.list();
    }

    /**
     * Get spelers (sorted)
     *
     * @param sorting int   Sorting index
     * @return spelers ArrayList<Speler>
     */
    public ArrayList<Speler> getSpelersSorted(int sorting) {
        ArrayList<Speler> spelers = new ArrayList<>(repository.list());

        switch (sorting) {
            case 1:
                Collections.sort(spelers, new SpelerComparator().sortByName());
                break;
            case 2:
                Collections.sort(spelers, new SpelerComparator().sortByScore());
                break;
        }

        return spelers;
    }

    /**
     * Add speler (on register)
     *
     * @see SpelerRepository
     * @param username String   Username
     * @param gender String     Gender
     * @return reponse JSONObject
     */
    public JSONObject addSpeler(String username, String gender) {
        // Initialize
        HttpRequest createUser = new HttpRequest("users", true);
        HashMap<String, String> data = new HashMap<>();

        // Set the required data
        data.put("username", username);
        data.put("gender", gender);

        // Post the data
        JSONObject response = createUser.postRequest(data);

        try {
            // If it has a response
            if (response.isNull("code")) {
                Speler speler = new Speler();
                speler.setId(response.getInt("id"));
                speler.setGebruikersnaam(response.getString("gebruikersnaam"));
                speler.setGeslacht(response.getString("geslacht"));
                speler.setGeld(response.getInt("geld"));
                speler.setSpeeltijd(response.getInt("speeltijd"));
                speler.setAvatar(response.getString("avatar"));
                speler.setOnline(response.getInt("online"));
                speler.setLaatstGezien(response.getString("laatst_gezien"));
                speler.setActief(response.getInt("actief"));
                speler.setXP(response.getInt("XP"));
                getDieren(speler);
                getUniekeDieren(speler);

                // Add the mederwerker to the repository
                repository.add(speler);
            } else {
                return response;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieve all users from database
     * And save them into the repository
     *
     * @see SpelerRepository
     * @return boolean
     */
    public boolean fetchSpelers() {
        int count = 0;
        HttpRequest getUsers = new HttpRequest("users", true);
        JSONObject users = getUsers.getRequest(true);

        for (int i = 0; i < users.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(users.getJSONObject(String.valueOf(i))));

                // Create the player
                Speler speler = new Speler();
                speler.setId(jsonData.getInt("id"));
                speler.setGebruikersnaam(jsonData.getString("gebruikersnaam"));
                speler.setGeslacht(jsonData.getString("geslacht"));
                speler.setGeld(jsonData.getInt("geld"));
                speler.setSpeeltijd(jsonData.getInt("speeltijd"));
                speler.setAvatar("http://" + jsonData.getString("avatar"));
                speler.setOnline(jsonData.getInt("online"));
                speler.setLaatstGezien(jsonData.getString("laatst_gezien"));
                speler.setActief(jsonData.getInt("actief"));
                speler.setXP(jsonData.getInt("XP"));
                getDieren(speler);
                getUniekeDieren(speler);

                // Add player object to repository
                repository.add(speler);

                // Count players
                count++;

                // Check whether fetching is done
                if (count == users.length() - OBJECT_MINUS_HTTP_RESPONSE) {
                    return true;
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * Set user logged in
     *
     * @param username String           Username of user
     */
    public void setSpelerLoggedIn(String username) {
        HttpRequest loginUser = new HttpRequest("users/login");
        HashMap<String, String> keyValue = new HashMap<>();

        keyValue.put("username", username);

        JSONObject jsonObject = loginUser.getRequestWithHeaders(keyValue, false);

        try {
            // If status: OK
            if (jsonObject.getInt("code") == 200) {
                Speler speler = getSpeler(getSpelers(), username);
                speler.setOnline(jsonObject.getInt("online"));
            } else {
                System.out.println(new TranslateHttpResponse(jsonObject.getString("code")).translateHttpResponseCode());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all animals which belongs to user
     *
     * @param speler Speler     User
     */
    public void getDieren(Speler speler) {
        ArrayList<Dier> dieren = new ArrayList<>();
        HttpRequest getAnimals = new HttpRequest("users/" + speler.getId() + "/animals", false);
        JSONObject response = getAnimals.getRequest(true);

        for (int i = 0; i < response.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(response.getJSONObject(String.valueOf(i))));

                Dier dier = new Dier();
                dier.setId(jsonData.getInt("id"));

                // Check whether animal has custom name set by user
                if (!jsonData.getString("aangepaste_naam").equals("null")) {
                    dier.setNaam(jsonData.getString("aangepaste_naam"));
                } else {
                    dier.setNaam(jsonData.getString("naam"));
                }

                dier.setAfbeelding(jsonData.getString("afbeelding"));
                dier.setAfbeeldingLocked(jsonData.getString("afbeelding_locked"));
                dier.setBeschrijving(jsonData.getString("beschrijving"));

                dieren.add(dier);
            } catch (JSONException e) {
                System.out.println("JSON Exception: " + e.getMessage());
            }
        }

        speler.setDieren(dieren);
    }

    /**
     * Get unique animal count which belongs to user
     *
     * @param speler Speler     User
     */
    public void getUniekeDieren(Speler speler) {
        /*
        MEHMETS OLD API SHIZZLE
        HttpRequest getAnimals = new HttpRequest("users/" + speler.getId() + "/animals?count=true", false);
        JSONObject response = getAnimals.getRequest(true);

        for (int i = 0; i < response.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(response.getJSONObject(String.valueOf(i))));

                speler.setUniekeDieren(jsonData.getInt("aantal_unieke_dieren"));
            } catch (JSONException e) {
                System.out.println("JSON Exception: " + e.getMessage());
            }
        }*/



        ArrayList<Integer> uniekeDierenID = new ArrayList<>();
        ArrayList<Dier> uniekeDieren = new ArrayList<>();
        for (Dier d : speler.getDieren()){
            if (!uniekeDierenID.contains(d.getId())){
                uniekeDierenID.add(d.getId());
                uniekeDieren.add(d);
            }
        }
        speler.setUniekeDieren(uniekeDieren);


        /*
        OMER TESTING
        ArrayList<Dier> uniekeDieren = new ArrayList<>();
        for (int i = 0; i < speler.getDieren().size(); i++){
            boolean doesExist = false;
            for (int x = 0; x < uniekeDieren.size(); x++){
                Log.d("SPELERCONTROLLER", speler.getDieren().get(i).toString() + "> > > " + uniekeDieren.get(x).toString());
                if (!speler.getDieren().get(i).equals(uniekeDieren.get(x))){
                    Log.d("SPELERCONTROLLER", speler.getDieren().get(i).toString() + " does not contain in " + uniekeDieren.get(x).toString());
                    doesExist = true;
                }
            }
            if (doesExist){
                uniekeDieren.add(speler.getDieren().get(i));
                doesExist = false;
            }
        }
        speler.setUniekeDieren(uniekeDieren);*/
    }

    /**
     * Upload selected avatar to the server
     *
     * @param context Context
     * @param path String       Image path
     */
    public void uploadAvatar(Context context, String path, int userId) {
        try {
            String uploadId = UUID.randomUUID().toString();

            // Make a POST call and save the chosen file
            new MultipartUploadRequest(context, uploadId, "http://:5000/api/v1/users/" + userId + "/avatar")
                    .addParameter("authentication", HttpRequest.AUTH_CREDENTIALS)
                    .addFileToUpload(path, "image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();

            // Get file name from path
            String filename = path.substring(path.lastIndexOf("/") + 1);

            getSpelerById(getSpelers(), userId).setAvatar("http://:5000/static/avatars/" + filename);
        } catch (Exception e) {
            System.out.println("Upload Exception: " + e.getMessage());
        }
    }

    /**
     * Display avatar of user
     *
     * @param speler Speler
     * @return avatar Bitmap
     */
    public Bitmap displayAvatar(Speler speler) {
        Bitmap avatar = null;

        try {
            // Get user avatar URL and establish a connection
            URL url = new URL(speler.getAvatar());

            // If found, retrieve the image
            avatar = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return avatar;
    }
}

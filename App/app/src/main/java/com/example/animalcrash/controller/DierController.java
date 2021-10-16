package com.example.animalcrash.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import com.example.animalcrash.model.Dier;
import com.example.animalcrash.repository.DierRepository;
import com.example.animalcrash.util.DierComparator;
import com.example.animalcrash.util.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Animal class methods
 *              Interaction between View & Repository
 *
 */

public class DierController {

    private final int OBJECT_MINUS_HTTP_RESPONSE = 2;
    private DierRepository repository;

    /**
     * Initialize controller
     */
    public DierController() {
        this.repository = new DierRepository();
    }

    /**
     * Get dier
     *
     * @see DierRepository
     * @param dieren ArrayList<Dier>
     * @param name String
     * @return Dier
     */
    public Dier getDier(ArrayList<Dier> dieren, String name) {
        return repository.get(dieren, name);
    }

    /**
     * Get dier by id
     *
     * @param animalId int
     * @return Dier
     */
    public Dier getDierById(int animalId) {
        return repository.getById(repository.list(), animalId);
    }

    /**
     * Get dieren
     *
     * @see DierRepository
     */
    public ArrayList<Dier> getDieren() {
        return repository.list();
    }

    /**
     * Get dieren (sorted)
     *
     * @param sorting int   Sorting index
     * @return dieren ArrayList<Dier>
     */
    public ArrayList<Dier> getDierenSorted(int sorting) {
        ArrayList<Dier> dieren = new ArrayList<>(repository.list());

        switch (sorting) {
            case 1:
                Collections.sort(dieren, new DierComparator().sortByName());
                break;
            case 2:
                Collections.sort(dieren, new DierComparator().sortByAmountOfCaught());
                break;
        }

        return dieren;
    }

    /**
     * Retrieve all dieren from database
     * And save them into the repository
     *
     * @see DierRepository
     * @return boolean
     */
    public boolean fetchDieren() {
        int count = 0;
        HttpRequest getAnimals = new HttpRequest("animals", true);
        JSONObject animals = getAnimals.getRequest(true);

        for (int i = 0; i < animals.length(); i++) {
            try {
                // Set response as JSON object
                JSONObject jsonData = new JSONObject(String.valueOf(animals.getJSONObject(String.valueOf(i))));

                // Create the animal
                Dier dier = new Dier();
                dier.setId(jsonData.getInt("id"));
                dier.setNaam(jsonData.getString("naam"));
                dier.setAfbeelding(jsonData.getString("afbeelding"));
                dier.setAfbeeldingLocked(jsonData.getString("afbeelding_locked"));
                dier.setBeschrijving(jsonData.getString("beschrijving"));

                // Add animal object to repository
                repository.add(dier);

                // Count animals
                count++;

                // Check whether fetching is done
                if (count == animals.length() - OBJECT_MINUS_HTTP_RESPONSE) {
                    return true;
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * Get animal image (unlocked)
     *
     * @param id int    Id of animal
     * @return Bitmap
     */
    public Bitmap getAnimalColouredImage(int id) {
        String imageURL = getDierById(id).getAfbeelding();

        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            return (Bitmap) BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get animal image (locked)
     *
     * @param id int    Id of animal
     * @return Bitmap
     */
    public Bitmap getAnimalGrayImage(int id) {
        String imageURL = getDierById(id).getAfbeelding();

        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            Bitmap bitmap =  BitmapFactory.decodeStream(input);

            // START TESTING
            int width, height;
            height = bitmap.getHeight();
            width = bitmap.getWidth();

            Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmpGrayscale);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bitmap, 0, 0, paint);
            // END TESTING

            return bmpGrayscale;
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return null;
    }
}

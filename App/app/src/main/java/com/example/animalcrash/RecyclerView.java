package com.example.animalcrash;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Dier;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.util.SessionManager;
import java.util.ArrayList;

public class RecyclerView extends AppCompatActivity {

    private static final String TAG = "RecyclerView";
    private DierController dierController = new DierController();

    private int aantalLeeuw = 0;
    private int aantalVos = 0;
    private int aantalNeushoorn = 0;
    private int aantalPaard = 0;
    private int aantalPanda = 0;

    //Zelfde variablen als in InventoryActivity om door te geven
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Bitmap> mImageBeeld = new ArrayList<>();
    private ArrayList<Integer> aantalDieren = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Log.d(TAG, "onCreate: started.");
        initImageBitmaps();


    }

    //vullen van de Arraylists
    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        Log.d(TAG, "animal (unlocked)");

        SpelerController spelerController = new SpelerController();
        SessionManager sessionManager = new SessionManager(this);
        Speler speler = spelerController.getSpeler(spelerController.getSpelers(),
                sessionManager.getStringValue("username"));

        Log.d("TEST", "----------------------------------------------------------------");
        Log.d("TEST", "The user has the following animals:");
        for (Dier sd : speler.getDieren()) {
            Log.d("TEST", sd.toString());

        }
        Log.d("TEST", "----------------------------------------------------------------");

        // Getting all animals
        for (Dier d : dierController.getDieren()) {
            // if player animals contains at least 1
            if (speler.getDieren().size() >= 1) {
                // Checking if the animal is caught;
                boolean animalCaught = false;
                // Getting all player animals
                for (Dier sd : speler.getDieren()) {
                    // If animal id is same with player animal id
                    if (d.getId() == sd.getId()) {
                        animalCaught = true;
                        switch (d.getId()) {
                            case 1:
                                aantalLeeuw++;
                                break;
                            case 2:
                                aantalVos++;
                                break;
                            case 3:
                                aantalPaard++;
                                break;
                            case 4:
                                aantalPanda++;
                                break;
                            case 5:
                                aantalNeushoorn++;
                                break;
                            default:
                                break;
                        }
                    }

                }
                if (animalCaught) {
                    mImageBeeld.add(dierController.getAnimalColouredImage(d.getId()));
                    mNames.add(d.getNaam());

                } else {
                    mImageBeeld.add(dierController.getAnimalGrayImage(d.getId()));
                    mNames.add(d.getNaam());
                }

            } else {
                mImageBeeld.add(dierController.getAnimalGrayImage(d.getId()));
                mNames.add(d.getNaam());

            }

        }
        // Aantal dieren toevoegen aan de array aantalDieren
        aantalDieren.add(aantalLeeuw);
        aantalDieren.add(aantalVos);
        aantalDieren.add(aantalPaard);
        aantalDieren.add(aantalPanda);
        aantalDieren.add(aantalNeushoorn);

        initRecyclerView();

    }

    // Methode voor het opzetten van de RecyclerView, constructor van InventoryActivity.java
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        android.support.v7.widget.RecyclerView recyclerView = findViewById(R.id.recycle_view);
        InventoryActivity adapter = new InventoryActivity(this, mNames, mImageBeeld, aantalDieren);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goBack(View view) {
        this.finish();
    }
}


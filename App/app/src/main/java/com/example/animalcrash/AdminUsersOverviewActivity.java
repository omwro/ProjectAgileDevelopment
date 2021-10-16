package com.example.animalcrash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Admin users overview view methods
 *
 */

public class AdminUsersOverviewActivity extends AppCompatActivity {

    // Initiliaze controller
    private SpelerController spelerController = new SpelerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_overview);

        sortName(null);
    }

    /**
     * Sort users by name
     *
     * @param view View
     */
    public void sortName(View view) {
        final ListView listView = (ListView) findViewById(R.id.users_overview);

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // Foreach speler, retrieve data and add it to the list
        for (Speler speler : spelerController.getSpelersSorted(1)) {
            HashMap<String, String> results = new HashMap<>();
            results.put("First Line", speler.getGebruikersnaam());
            results.put("Second Line", speler.getDataAndScoreToString());
            listItems.add(results);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listview_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.header, R.id.body});

        // Set data to the list view
        listView.setAdapter(simpleAdapter);

        // Display message
        Toast.makeText(this, "Spelers sorteren op alfabetische volgorde", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sort users by caught animals (score)
     *
     * @param view View
     */
    public void sortScore(View view) {
        final ListView listView = (ListView) findViewById(R.id.users_overview);

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // Foreach speler, retrieve data and add it to the list
        for (Speler speler : spelerController.getSpelersSorted(2)) {
            HashMap<String, String> results = new HashMap<>();
            results.put("First Line", speler.getGebruikersnaam());
            results.put("Second Line", speler.getDataAndScoreToString());
            listItems.add(results);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listview_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.header, R.id.body});

        // Set data to the list view
        listView.setAdapter(simpleAdapter);

        // Display message
        Toast.makeText(this, "Spelers sorteren op aantal gevangen dieren", Toast.LENGTH_SHORT).show();
    }

    public void addUser(View view){
        MainActivity.getInstance().goToAdminRegisterScreen();
    }

    /**
     * Finish view and go back
     *
     * @param view View
     */
    public void goBack(View view) {
        this.finish();
    }
}

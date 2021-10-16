package com.example.animalcrash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.model.Dier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Admin animals overview view methods
 *
 */

public class AdminAnimalsOverviewActivity extends AppCompatActivity {

    // Initiliaze controller
    private DierController dierController = new DierController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_animals_overview);

        sortName(null);
    }

    /**
     * Sort animals by name
     *
     * @param view View
     */
    public void sortName(View view) {
        final ListView listView = (ListView) findViewById(R.id.animals_overview);

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // Foreach dier, retrieve data and add it to the list
        for (Dier dier : dierController.getDierenSorted(1)) {
            HashMap<String, String> results = new HashMap<>();
            results.put("First Line", dier.getNaam());
            results.put("Second Line", dier.getData());
            listItems.add(results);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listview_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.header, R.id.body});

        // Set data to the list view
        listView.setAdapter(simpleAdapter);

        // Display message
        Toast.makeText(this, "Dieren sorteren op alfabetische volgorde", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sort by most caught animals
     *
     * @param view View
     */
    public void sortScore(View view) {
        final ListView listView = (ListView) findViewById(R.id.animals_overview);

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // Foreach dier, retrieve data and add it to the list
        for (Dier dier : dierController.getDierenSorted(2)) {
            HashMap<String, String> results = new HashMap<>();
            results.put("First Line", dier.getNaam());
            results.put("Second Line", dier.getData());
            listItems.add(results);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listview_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.header, R.id.body});

        // Set data to the list view
        listView.setAdapter(simpleAdapter);

        // Display message
        Toast.makeText(this, "Dieren sorteren op meest gevangen", Toast.LENGTH_SHORT).show();
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

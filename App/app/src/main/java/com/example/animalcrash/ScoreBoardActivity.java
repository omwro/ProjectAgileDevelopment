package com.example.animalcrash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.util.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity {

    private SpelerController spelerController = new SpelerController();
    private SessionManager sessionManager;
    private Speler currentSpeler;
    private ArrayList<Speler> spelerArray = spelerController.getSpelersSorted(2);
    private TextView currentScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.sessionManager = new SessionManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        currentSpeler = spelerController.getSpeler(spelerController.getSpelers(), sessionManager.getStringValue("username"));
        currentScore = findViewById(R.id.score);
        sortPlayers();
        getCurrentPlayerScore();
    }

    /**
     * Sorts the players by current score
     */
    private void sortPlayers (){
        final ListView listView = findViewById(R.id.users_overview);

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // Haal elke speler op en voeg alle data toe
        for (Speler speler : spelerArray) {
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
    }

    /**
     * Displays the current place of the player
      */
    private void getCurrentPlayerScore(){
        for(int i = 0; i < spelerArray.size(); i++){
            if(spelerArray.get(i).getId() == currentSpeler.getId()){
                i ++;
                currentScore.setText("U staat op de " + i + "e plek!");
                break;
            }
        }
    }

    public void goBack(View view) {
        this.finish();
    }
}

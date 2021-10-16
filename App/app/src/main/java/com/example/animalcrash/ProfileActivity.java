package com.example.animalcrash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.model.SpelerLevel;
import com.example.animalcrash.util.SessionManager;

/**
 * @author Omer Erdem 500802898 IS107
 * @version 1.0
 * ProfileActivity is een class dat de profiel pagina bestuurd. Het heeft als doel om alle benodigde
 * informatie op te halen en daarna het informatie opslaan binnen de parameters, berekeningen maken
 * en als laatste alles tonen op de pagina.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameLabel;
    private TextView levelLabel;
    private ProgressBar levelXpBarLabel;
    private TextView xpLeftLabel;
    private TextView xpNeededLabel;
    private ProgressBar amountOfAnimalsBarLabel;
    private TextView amountOfAnimalsLabel;
    private TextView moneyLabel;
    private TextView amountOfPlayedLabel;

    private SpelerController spelerController;
    private DierController dierController;
    private SessionManager sessionManager;

    /**
     * Hieronder laden we het scherm en geven op basis van de connectie aan welke speler informatie
     * we gaan gebruiken voor de applicatie
     *
     * @param savedInstanceState is een object die aan elk activiteitescherm wordt gegeven
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize controllers & session manager
        this.spelerController = new SpelerController();
        this.dierController = new DierController();
        this.sessionManager = new SessionManager(this);

        Speler loggedInUser = spelerController.getSpeler(spelerController.getSpelers(),
                sessionManager.getStringValue("username"));

        usernameLabel = findViewById(R.id.username);
        levelLabel = findViewById(R.id.levellbl);
        xpLeftLabel = findViewById(R.id.xpleftlbl);
        levelXpBarLabel = findViewById(R.id.progressBar);
        xpNeededLabel = findViewById(R.id.xpneededlbl);
        amountOfAnimalsBarLabel = findViewById(R.id.circularProgressbar);
        amountOfAnimalsLabel = findViewById(R.id.dierenlbl);
        moneyLabel = findViewById(R.id.geldlbl);
        amountOfPlayedLabel = findViewById(R.id.urenlbl);

        SpelerLevel spelerLevel = new SpelerLevel(loggedInUser.getXP());

        spelerController.getUniekeDieren(loggedInUser);

        Log.d("TESTING ANIMALS", "User unique animals(" + loggedInUser.getUniekeDieren().size() + "): " + loggedInUser.getUniekeDieren());
        Log.d("TESTING ANIMALS", "User all animals(" + loggedInUser.getDieren().size() + "): " + loggedInUser.getDieren());
        Log.d("TESTING ANIMALS", "system all animals(" + dierController.getDieren().size() + "): " + dierController.getDieren());

        usernameLabel.setText(loggedInUser.getGebruikersnaam());
        levelLabel.setText(String.valueOf(spelerLevel.getLevel()));
        xpLeftLabel.setText(String.valueOf(spelerLevel.getXpLeft()));
        levelXpBarLabel.setProgress(spelerLevel.getProgress());
        xpNeededLabel.setText(String.valueOf(spelerLevel.getXpNeeded()));
        amountOfAnimalsBarLabel.setProgress(loggedInUser.getUniekeDieren().size());
        amountOfAnimalsBarLabel.setMax(dierController.getDieren().size());
        String amountOfAnimalsString = String.valueOf(loggedInUser.getUniekeDieren().size()) + '/' +
                dierController.getDieren().size();
        amountOfAnimalsLabel.setText(amountOfAnimalsString);
        moneyLabel.setText(String.valueOf(loggedInUser.getGeld()));
        amountOfPlayedLabel.setText(String.valueOf(loggedInUser.getSpeeltijd()));
    }

    /**
     * Hieronder geef je aan dat de als je op de terug knop drukt op de pagina dat je terug gaat
     * naar de maps pagina
     *
     * @param view wordt gebruikt als je een knop maakt in xml
     */
    public void goBack(View view) {
        this.finish();
    }

    /**
     * Log user out
     *
     * @param view View
     */
    public void logout(View view) {
        sessionManager.clear();
        MainActivity.getInstance().goToMainScreen();
    }
}

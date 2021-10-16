package com.example.animalcrash;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.util.Popup;
import com.example.animalcrash.util.SessionManager;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Login view methods
 *
 */

public class LoginActivity extends AppCompatActivity {

    private SpelerController spelerController;
    private DierController dierController;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize controller & session manager
        this.spelerController = new SpelerController();
        this.dierController = new DierController();
        this.sessionManager = new SessionManager(this);
        this.sessionManager.clear();

        // If users are not initialized
        // Initialize them and store it
        try {
            if (spelerController.getSpelers().isEmpty()) {
                spelerController.fetchSpelers();
            }

            if (dierController.getDieren().isEmpty()) {
                dierController.fetchDieren();
            }
        } catch (Exception ex) {
            String title = "Er ging iets fout";
            String message = "Er is een onbekende probleem op de server ontstaan, probeer later opnieuw.";

            Popup popup = new Popup(this, title, message, true);
            popup.create()
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
    }

    /**
     * Retrieve username textfield value (lowercase)
     *
     * @return usernameInput String
     */
    public String getGebruikersnaam() {
        EditText input = findViewById(R.id.usernameInput);

        return input.getText().toString().toLowerCase();
    }

    /**
     * On button login action
     *
     * @param view View
     */
    public void onLoginButton(View view) {
        String title = null;
        String message = null;

        if (getGebruikersnaam().equals("")) {
            Popup popup = new Popup(this, "Er ging iets mis",
                    "Voer gebruikersnaam in", true);

            popup.create()
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();

            return;
        }

        Speler speler = spelerController.getSpeler(spelerController.getSpelers(), getGebruikersnaam());

        if (speler != null) {
            if (speler.isOnline() != 1) {
                if (speler.isActief() == 1) {
                    // TO BE ADDED ONCE THE APP IS FULLY WORKING
                    /* spelerController.setSpelerLoggedIn(getGebruikersnaam()); */
                    sessionManager.setStringValue("username", getGebruikersnaam());
                    MainActivity.getInstance().goToSplashScreen();
                    return;
                } else {
                    title = "Er ging iets mis";
                    message = "Dit account is geblokkeerd.";
                }
            } else {
                title = "Er ging iets mis";
                message = "Dit account is al ingelogd, probeer later opnieuw.";
            }
        } else {
            title = "Er ging iets mis";
            message = "Er bestaat geen account met de door u ingevoerde gebruikersnaam.";
        }

        Popup popup = new Popup(this, title, message, true);

        popup.create()
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * Redirect to admin login view
     *
     * @param view View
     */
    public void goToAdminLoginScreen(View view) {
        MainActivity.getInstance().goToAdminLoginScreen();
    }
}

package com.example.animalcrash;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.controller.MedewerkerController;
import com.example.animalcrash.model.Medewerker;
import com.example.animalcrash.util.Popup;
import com.example.animalcrash.util.SessionManager;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Login view methods
 *
 */

public class AdminLoginActivity extends AppCompatActivity {

    private MedewerkerController medewerkerController;
    private DierController dierController;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_screen);

        // Initialize controllers & session manager
        this.medewerkerController = new MedewerkerController();
        this.dierController = new DierController();
        this.sessionManager = new SessionManager(this);
        this.sessionManager.clear();

        // If employees & animals are not initialized
        // Initialize them and store it
        try {
            if (medewerkerController.getMedewerkers().isEmpty()) {
                medewerkerController.fetchMedewerkers();
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

        Medewerker medewerker = medewerkerController.getMedewerker(medewerkerController.getMedewerkers(), getGebruikersnaam());

        if (medewerker != null) {
            sessionManager.setStringValue("username", getGebruikersnaam());
            sessionManager.setBooleanValue("admin", true);
            MainActivity.getInstance().goToAdminDashboard();
            return;
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
     * Redirect to login screen
     *
     * @param view View
     */
    public void goToLoginScreen(View view) {
        MainActivity.getInstance().goToLoginScreen(view);
    }
}

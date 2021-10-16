package com.example.animalcrash;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.util.Popup;
import com.example.animalcrash.util.SessionManager;
import com.example.animalcrash.util.TranslateHttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Register view methods
 *
 */

public class RegisterActivity extends AppCompatActivity {

    private SpelerController spelerController;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // Initialize controller & session
        this.spelerController = new SpelerController();
        this.sessionManager = new SessionManager(this);

        Spinner staticSpinner = (Spinner) findViewById(R.id.genderInput);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.genders,
                        R.layout.custom_spinner);

        staticAdapter.setDropDownViewResource(R.layout.custom_spinner);

        staticSpinner.setAdapter(staticAdapter);
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
     * Retrieve spinner value
     *
     * @return gender String
     */
    public String getGeslacht() {
        Spinner input = findViewById(R.id.genderInput);

        return (String) input.getSelectedItem();
    }

    /**
     * On button register action
     *
     * @param view View
     */
    public void onRegisterButton(View view) {
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

        final JSONObject response = spelerController.addSpeler(getGebruikersnaam(), getGeslacht());

        try {
            if (response == null) {
                // TO BE ADDED ONCE THE APP IS FULLY WORKING
                /* spelerController.setSpelerLoggedIn(getGebruikersnaam()); */
                sessionManager.setStringValue("username", getGebruikersnaam());
                MainActivity.getInstance().goToSplashScreen();
                return;
            } else {
                title = new TranslateHttpResponse(response.getString("code")).translateHttpResponseCode();
                message = response.getString("response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

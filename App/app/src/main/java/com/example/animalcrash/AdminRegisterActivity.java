package com.example.animalcrash;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.animalcrash.controller.MedewerkerController;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.util.Popup;
import com.example.animalcrash.util.TranslateHttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminRegisterActivity extends AppCompatActivity {

    private SpelerController spelerController;
    private MedewerkerController medewerkerController;

    private EditText gebruikernaam;
    private Spinner gender;
    private Spinner type;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        // Initialize
        spelerController = new SpelerController();
        medewerkerController = new MedewerkerController();

        gebruikernaam = findViewById(R.id.usernameInput);

        gender = (Spinner) findViewById(R.id.genderInput);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter
                .createFromResource(this, R.array.genders, R.layout.custom_spinner);
        genderAdapter.setDropDownViewResource(R.layout.custom_spinner);
        gender.setAdapter(genderAdapter);

        type = (Spinner) findViewById(R.id.userTypeInput);
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter
                .createFromResource(this, R.array.user_type, R.layout.custom_spinner);
        userTypeAdapter.setDropDownViewResource(R.layout.custom_spinner);
        type.setAdapter(userTypeAdapter);

        email = findViewById(R.id.emailInput);

        // User type spinner OnChange event
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (type.getSelectedItem().toString().equals("Gebruiker")) {
                    // Hides the email inputfield
                    email.setVisibility(View.INVISIBLE);
                } else if (type.getSelectedItem().toString().equals("Medewerker")) {
                    // Unhides the email inputfield
                    email.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void onRegisterButton(final View view) {
        // Initialize to String
        String gebruikersnaam = this.gebruikernaam.getText().toString();
        String geslacht = this.gender.getSelectedItem().toString();
        String type = this.type.getSelectedItem().toString();
        String email = this.email.getText().toString();

        // Initialize for the pop-up header
        String title = null;
        String message = null;

        // Checking if the username is not empty
        if (gebruikersnaam.equals("")) {
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

        final JSONObject response;

        // Checking the type of the account
        if (type.equals("Gebruiker")) {
            response = spelerController.addSpeler(gebruikersnaam, geslacht);
        } else if (type.equals("Medewerker")) {
            response = medewerkerController.addMedewerker(gebruikersnaam, geslacht, email);

        } else {
            response = null;
        }

        // If succeeded set popup headers, else error message as popup header
        try {
            if (response == null) {
                title = "Gelukt";
                message = gebruikersnaam + " is aangemaakt";
            } else {
                title = new TranslateHttpResponse(response.getString("code")).translateHttpResponseCode();
                message = response.getString("response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the popup
        Popup popup = new Popup(this, title, message, true);

        popup.create()
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        goBack(view);
                    }
                }).create().show();
    }

    // Back button
    public void goBack(View view) {
        this.finish();
    }
}

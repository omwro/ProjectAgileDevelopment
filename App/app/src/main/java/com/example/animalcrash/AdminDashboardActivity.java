package com.example.animalcrash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.animalcrash.util.SessionManager;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Admin dashboard view methods
 *
 */

public class AdminDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize session
        this.sessionManager = new SessionManager(this);
    }

    /**
     * Redirect to maps screen
     *
     * @param view View
     */
    public void goToMaps(View view) {
        MainActivity.getInstance().goToAdminMapScreen();
    }

    /**
     * Redirect to user overview
     *
     * @param view View
     */
    public void goToUserOverview(View view) {
        MainActivity.getInstance().goToAdminUserOverview();
    }

    /**
     * Redirect to animal overview
     *
     * @param view View
     */
    public void goToAdminAnimalOverview(View view) {
        MainActivity.getInstance().goToAdminAnimalOverview();
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

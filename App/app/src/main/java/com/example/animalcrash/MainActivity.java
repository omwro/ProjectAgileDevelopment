package com.example.animalcrash;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.animalcrash.controller.SpelerController;
import com.example.animalcrash.model.Speler;
import com.example.animalcrash.util.RequestPermission;
import com.example.animalcrash.util.SessionManager;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instance = this;

        // Initialize session manager
        this.sessionManager = new SessionManager(this);

        // Disable the strict policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Clear sessions
        // sessionManager.clear();
    }

    /**
     * Main screen view
     */
    public void goToMainScreen() {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    // opening Splashscreen after buttonclick
    public void goToSplashScreen() {
        Intent intent = new Intent (this, SplashScreen.class);
        startActivity(intent);
    }

    public void goToMapScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    /**
     * Login view
     *
     * @param view View
     */
    public void goToLoginScreen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Admin login view
     */
    public void goToAdminLoginScreen() {
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }

    /**
     * Register view
     */
    public void goToRegisterScreen() {
        Intent intent = new Intent (this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Admin dashboard view
     */
    public void goToAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }

    /**
     * Admin maps view
     */
    public void goToAdminMapScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    /**
     * Admin users overview view
     */
    public void goToAdminUserOverview() {
        Intent intent = new Intent(this, AdminUsersOverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Admin register view
     */
    public void goToAdminRegisterScreen() {
        Intent intent = new Intent(this, AdminRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Admin placed animals overview view
     */
    public void goToAdminAnimalOverview() {
        Intent intent = new Intent(this, AdminAnimalsOverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Get permission of user and upload image
     *
     * @param view View
     */
    public void uploadImage(View view) {
        if (new RequestPermission().requestStoragePermission(this, this)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Selecteer een optie"), 3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("MAINACTIVITY - SESSION MANAGER: USERNAME = " + sessionManager.getStringValue("username"));

        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            SpelerController spelerController = new SpelerController();
            Uri filePath = data.getData();

            try {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Speler speler = spelerController.getSpeler(spelerController.getSpelers(), sessionManager.getStringValue("username"));
                if (speler != null) {
//                    image.setImageBitmap(bitmap);
                    System.out.println("MAIN ACTIVITY DISPLAY AVATAR: " + speler.getAvatar());
                    try {
                        spelerController.uploadAvatar(this, getPath(filePath), speler.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        image.setImageBitmap(spelerController.displayAvatar(speler));
                    }
                } else {
                    System.out.println("NOT LOGGED IN, SESSION USERNAME: " + sessionManager.getStringValue("username"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    }

    /**
     * Get path of image
     *
     * @param uri Uri
     * @return path String      Path of selected image
     */
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document = cursor.getString(0);
        document = document.substring(document.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}


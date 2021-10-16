package com.example.animalcrash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progress = 0;

    @Override
    // Starting the Splash Screen
    // Defining the Progress bar
    // Running the Timer function
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
        timer();
    }

    // Timer running 5 times
    // Timer delay of 500 ms
    // On the 5th time starting the Maps activity
    public void timer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress += 25;
                progressBar.setProgress(progress);
                if (progress < 101){
                    timer();
                } else{
                    goToMaps();
                }
            }
        }, 500);
    }

    // Method to open the Maps activity
    public void goToMaps (){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}


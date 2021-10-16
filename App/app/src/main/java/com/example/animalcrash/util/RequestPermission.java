package com.example.animalcrash.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Retrieve request permissions
 *
 */

public class RequestPermission {

    /**
     * Request storage permission
     *
     * @param context Context
     * @param activity Activity
     * @return boolean
     */
    public boolean requestStoragePermission(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;

        /*
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Explain why the permission is required
        }
        */

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

        return false;
    }
}

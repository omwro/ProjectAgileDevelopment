package com.example.animalcrash.util;

import android.app.AlertDialog;
import android.content.Context;
import com.example.animalcrash.R;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Pop-up methods
 *
 */

public class Popup {

    private Context context;
    private String title;
    private String message;
    private boolean cancelable;

    /**
     * Initialize popup
     *
     * @param context Context
     * @param title String      Popup title
     * @param message String    Popup message
     */
    public Popup(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.cancelable = false;
    }

    /**
     * Initialize popup
     *
     * @param context Context
     * @param title String          Popup title
     * @param message String        Popup message
     * @param cancelable boolean    Cancelable
     */
    public Popup(Context context, String title, String message, boolean cancelable) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.cancelable = cancelable;
    }

    /**
     * Build popup
     *
     * @return popup AlertDialog.Builder
     */
    public AlertDialog.Builder create() {
        AlertDialog.Builder popup = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message);

        return popup;
    }
}

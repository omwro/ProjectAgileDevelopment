package com.example.animalcrash.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Session manager, maintain sessions
 *
 */

public class SessionManager {

    private static SharedPreferences preferences;

    /**
     * Initialize session manager
     *
     * @param context Context
     */
    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set string value
     *
     * @param key String    Key
     * @param value String  Value
     */
    public void setStringValue(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    /**
     * Set boolean value
     *
     * @param key String    Key
     * @param value boolean Value
     */
    public void setBooleanValue(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    /**
     * Set int value
     *
     * @param key String    Key
     * @param value int     Value
     */
    public void setIntValue(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    /**
     * Get string value
     *
     * @param key String    Key
     * @return value String
     */
    public String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    /**
     * Get boolean value
     *
     * @param key String    Key
     * @return value boolean
     */
    public boolean getBooleanValue(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * Get int value
     *
     * @param key String    Key
     * @return value int
     */
    public int getIntValue(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Clear all session keys & values
     */
    public void clear() {
        preferences.edit().clear().commit();
    }
}

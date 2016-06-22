package com.erbene.popularmovies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Maia on 6/8/2016.
 */
public class SettingsActivity extends Activity {

    private final static String TAG = "SettingsActivity";
    public final static String KEY_ORDER_BY = "pref_order_key";
    public final static String KEY_FAVORITE = "pref_favorite";
    public SettingsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new OrderFragment()).commit();
    }



    public static class OrderFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final static String TAG = "OrderFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate");
            addPreferencesFromResource(R.xml.preferences);
        }

        public OrderFragment() {
            super();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i("SettingsFragment","ONSHAREDPREFENCES");
            if (key.equals(KEY_ORDER_BY)) {
                Preference connectionPref = findPreference(key);
                connectionPref.setSummary(sharedPreferences.getString(key, ""));
            }
        }
    }

}
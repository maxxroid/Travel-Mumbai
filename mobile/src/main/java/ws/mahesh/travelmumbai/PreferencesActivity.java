package ws.mahesh.travelmumbai;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * Created by Mahesh on 7/17/2014.
 */
public class PreferencesActivity extends PreferenceActivity {
    SharedPreferences.Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_holder);
        addPreferencesFromResource(R.xml.main_preferences);

        SharedPreferences pref = getSharedPreferences("user_settings", 0); // 0 - for private mode
        editor = pref.edit();
        final CheckBoxPreference Notification = (CheckBoxPreference) getPreferenceManager().findPreference("Notification");
        Notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                editor.putBoolean("Notification", (Boolean) newValue);
                editor.commit();
                return true;
            }
        });

        Preference CurrentVersion = findPreference("CurrentVersion");
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            CurrentVersion.setSummary("v" + versionName);
            CurrentVersion.setTitle("Application Version");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Preference AboutApp = findPreference("AboutApp");
        AboutApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // startActivity(new Intent(PreferencesActivity.this, AboutVITACMActivity.class));
                return false;
            }
        });

        editor.commit();
    }
}
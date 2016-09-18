package poc.test.lotroclock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
    	
    	ListPreference periodsInitialization = (ListPreference) findPreference("periods_initialization");
    	periodsInitialization.setOnPreferenceChangeListener(this);
        
       
        
    }

	

	@Override
	public boolean onPreferenceChange(Preference pref, Object value) {
		ListPreference listPref = (ListPreference) pref;
		String periodName = (String) value;
		
		long timeNow = System.currentTimeMillis();
		
		int periodId = listPref.findIndexOfValue(periodName);
		
		long timeCumulated = 0 ;
		if (periodId != 0) {
			for (int i = 0; i < periodId; i++) {
				timeCumulated = timeCumulated + MainActivity.PERIODS[i][1] ;
			}
		}
		Long timeLotroStart = timeNow - timeCumulated ;
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong("LotroStartTime", timeLotroStart);
		editor.commit();

		
		Toast.makeText(this, "Initialization complete. You should reschedule existing notifications.", Toast.LENGTH_LONG).show();
		
		return true;
	}

	
}

package poc.test.lotroclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
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
public class Notifications extends PreferenceActivity implements OnPreferenceChangeListener{
	
	CheckBoxPreference notificationEnabled ;
	ListPreference notificationPeriod ;
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.pref_notification);
	        
	        notificationEnabled = (CheckBoxPreference) findPreference("notifications_new_message");
	        notificationEnabled.setOnPreferenceChangeListener(this);
	    	
	    	notificationPeriod = (ListPreference) findPreference("period_notification");
	    	notificationPeriod.setOnPreferenceChangeListener(this);
	        
	    	
	    }

		

		@Override
		public boolean onPreferenceChange(Preference pref, Object value) {
			
			AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			Intent sendNotification  = new Intent(this, AlarmReceiver.class);
			
			PendingIntent alarmIntent2 = PendingIntent.getService(this, 5, sendNotification, PendingIntent.FLAG_UPDATE_CURRENT);
						

			alarmManager.cancel(alarmIntent2);
			
						
			if (pref.equals(notificationPeriod)) {
							
				ListPreference listPref = (ListPreference) pref;
				String periodName = (String) value;
				sendNotification.putExtra("period", periodName);
				
				int periodId = listPref.findIndexOfValue(periodName);
				
				long timeOfChosenPeriod = 0 ;
				if (periodId != 0) {
					for (int i = 0; i < periodId; i++) {
						timeOfChosenPeriod = timeOfChosenPeriod + MainActivity.PERIODS[i][1] ;
					}
				}
				
							
				long timeUntilAlarm = 0 ;
				long lotroTime = MainActivity.calculateLotrotimeMethod() ;
				
				if (lotroTime < timeOfChosenPeriod) {
					timeUntilAlarm = timeOfChosenPeriod - lotroTime;
				} else {
					timeUntilAlarm = MainActivity.LOTRO_DAY
							- (lotroTime - timeOfChosenPeriod);
				}
		
				// set the first notification and repeating every Lotroday 
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeUntilAlarm,
						MainActivity.LOTRO_DAY , alarmIntent2);
				
				Toast.makeText(this, "Notification set in " + (timeUntilAlarm / (1000 * 60))  + " minutes", Toast.LENGTH_LONG).show();
				}
			
			
			
			return true;
		}

		
	}

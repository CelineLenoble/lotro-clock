package poc.test.lotroclock;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;


public class AlarmReceiver extends Service {
	


	
	@Override
	public void onCreate() {


		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("lotro", "entering alarm receiver");
		handleCommand(intent);
		

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
		
		
	}
	
	private void handleCommand(Intent intent) {
		
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this) ;
		
		String periodChosen = preference.getString("period_notification", null);
		
		
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent resultPendingIntent =
			    PendingIntent.getActivity(
			    this,
			    0,
			    resultIntent,
			    PendingIntent.FLAG_UPDATE_CURRENT
			);
		
				
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(android.R.drawable.ic_menu_recent_history)
			    .setContentTitle(getString(R.string.app_name))
			    .setContentText(getString(R.string.notificationtext, periodChosen))
			    .setTicker(periodChosen)
			    .setAutoCancel(true)
			    .setContentIntent(resultPendingIntent);
		

		
		// Sets an ID for the notification
		int mNotificationId = 5;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	


	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}

	

	
		
	}

	
	
	
	


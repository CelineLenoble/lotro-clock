package poc.test.lotroclock;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

	/** Periods duration and images table */	
	public static final int[][] PERIODS = new int[][] {
			{R.drawable.im_dawn, 		((9 * 60) + 32) * 1000},		
			{R.drawable.im_morning, 	((28 * 60) + 42) * 1000},	
			{R.drawable.im_noon, 		((17 * 60) + 47) * 1000},	
			{R.drawable.im_afternoon, 	((27 * 60) + 58) * 1000},	
			{R.drawable.im_dusk, 		((18 * 60) + 21) * 1000},	
			{R.drawable.im_gloaming, 	((9 * 60) + 30) * 1000},		
			{R.drawable.im_evening, 	((27 * 60) + 59) * 1000},	
			{R.drawable.im_midnight, 	((8 * 60) + 59) * 1000},	
			{R.drawable.im_latewatches, ((19 * 60) + 1) * 1000},	
			{R.drawable.im_foredawn, 	((18 * 60) + 11) * 1000},		
	};

	public static final long LOTRO_DAY = ((180 * 60) + 360 )* 1000 ;
	

	
	public static long timeLotroStart ;

	TextView lotroPeriodTextView ;
	TextView nextPeriodTextView ;
	TextView timeBeforeTextView ;
	RelativeLayout layoutRelativeLayout ;
	

	int currentPeriodId = -1 ;

	long periodsCumulated = -1 ;
	
	long lotroTime  ;
	
	SharedPreferences preferences ;
	
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		preferences = PreferenceManager.getDefaultSharedPreferences(this) ;

		
		

		lotroPeriodTextView = (TextView) findViewById(R.id.lotroperiod) ;
		nextPeriodTextView = (TextView) findViewById(R.id.nextperiod);
		timeBeforeTextView = (TextView) findViewById(R.id.timebeforenext) ;
		layoutRelativeLayout = (RelativeLayout) findViewById(R.id.layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.refresh:
	            refresh();
	            return true;
	        case R.id.initialization:
	            initialization();
	            return true;
	        case R.id.notifications:
	            notifications();
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		currentPeriodId = -1 ;
		lotroTime = -1 ;
		periodsCumulated = -1 ;
		
		timeLotroStart = preferences.getLong("LotroStartTime", 13840884 * 100000) ;
		Log.d("lotro", "entering on resume: time:" + timeLotroStart);
		
		//Intent intent = getIntent();
		// String message = intent.getStringExtra(MainActivity.EXTRA_PERIODID);
			
		//Calculate lotrotime
		
		lotroTime = calculateLotrotimeMethod ();
		
		
		//Calculate current period
		calculatePeriodMethod() ;
		
		
		//Calculate time before next period
		String timebefore = timeBeforeNextPeriodMethod() ;
		
				
		//Updating display depending on time in lotro
		
		updateDisplayMethod(timebefore);
		
	}
	
	
	//Method to refresh update
		public void refresh () {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	
	//Method to open initialization
	public void initialization () {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	
	// Method to open notifications manager
	public void notifications () {
		Intent intent = new Intent(this, Notifications.class);
		startActivity(intent);
	}
	
	
		
	
	//Method to calculate lotrotime
	
	public static long calculateLotrotimeMethod () {
				
		long timeNow = System.currentTimeMillis();
				
		return ((timeNow - timeLotroStart) % LOTRO_DAY );
	}
	
	
	//Method to calculate current period
	
	void  calculatePeriodMethod(){
		
		
		while (lotroTime > periodsCumulated ) {
			currentPeriodId++ ;
			periodsCumulated = periodsCumulated + PERIODS[currentPeriodId][1];
			
		}
		
			
				
	} 
	
	//Method to calculate time before next period
	
	String timeBeforeNextPeriodMethod() {
			
		
		long timebeforenext = periodsCumulated - lotroTime ;
		long dureesecondes = timebeforenext / 1000 ;
		long nbrminutes = dureesecondes / 60 ;
		long nbrsecondes = dureesecondes % 60;
		
		String timebefore = nbrminutes + " minutes " + nbrsecondes + " secondes" ;
		
		return timebefore ;
	}
	
	//Method to update display
	
	void updateDisplayMethod (String timebefore ) {

		String[] listPeriods = getResources().getStringArray(R.array.listperiods);
		layoutRelativeLayout.setBackgroundResource(PERIODS[currentPeriodId][0]);
		lotroPeriodTextView.setText(listPeriods[currentPeriodId]);
		if (currentPeriodId < listPeriods.length -1){
			nextPeriodTextView.setText(listPeriods[currentPeriodId + 1]);
		} else {
			nextPeriodTextView.setText(listPeriods[0]);
		}
		
		timeBeforeTextView.setText(timebefore) ;
	}
	
}



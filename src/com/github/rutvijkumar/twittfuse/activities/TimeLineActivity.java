package com.github.rutvijkumar.twittfuse.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.fragments.TweetListFragment;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.services.OfflineTweetAlarmReceiver;

/***
 * Users Home Time Line Activity
 * <p>
 *  	Activity list all tweets on users timeline in endless paginated manner.
 * </p
 * @author Rutvijkumar Shah
 *
 */
public class TimeLineActivity extends FragmentActivity implements OnNewTweetListener{

	private TweetListFragment tweetListFragment;
	
	@Override
	public void onNewTweet(Tweet tweet) {
		//RFIXTHIS
		if(tweetListFragment!=null)
		tweetListFragment.addNewTweet(tweet);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		scheduleAlarm();
	  }

	
	  public void scheduleAlarm() {
	    // Construct an intent that will execute the AlarmReceiver
	    Intent intent = new Intent(getApplicationContext(), OfflineTweetAlarmReceiver.class);
	    
	    // Create a PendingIntent to be triggered when the alarm goes off
	    final PendingIntent pIntent = PendingIntent.getBroadcast(this, OfflineTweetAlarmReceiver.REQUEST_CODE,
	        intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    // Setup periodic alarm every 10 seconds
	    long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
	    int intervalMillis = 10000; // 10 seconds
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
	  }
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_compose) {
			Util.onCompose(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
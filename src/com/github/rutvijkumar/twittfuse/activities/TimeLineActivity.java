package com.github.rutvijkumar.twittfuse.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.fragments.HomeTimeLineFragment;
import com.github.rutvijkumar.twittfuse.fragments.MentionsFragment;
import com.github.rutvijkumar.twittfuse.fragments.TweetListFragment;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.services.OfflineTweetAlarmReceiver;

/***
 * Users Home Time Line Activity
 * <p>
 * Activity list all tweets on users timeline in endless paginated manner.
 * </p
 * 
 * @author Rutvijkumar Shah
 * 
 */
public class TimeLineActivity extends FragmentActivity implements
		OnNewTweetListener {

	private TweetListFragment tweetListFragment;
	private MyPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;
	private static final int POSITION_OF_HOMETIMELINE = 0;
	private static final int POSITION_OF_MENTIONS = 1;

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 2;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case POSITION_OF_HOMETIMELINE: // Fragment # 0 - This will show
											// FirstFragment
				return HomeTimeLineFragment.newInstance(position, "Timeline");
			case POSITION_OF_MENTIONS: // Fragment # 0 - This will show
										// FirstFragment different title
				return MentionsFragment.newInstance(position, "Mention");

			default:
				return null;
			}
		}

		// Returns the page title for the top i;ndicator
		@Override
		public CharSequence getPageTitle(int position) {
			if (position == POSITION_OF_HOMETIMELINE) {
				return "Timeline";
			} else {
				return "Mentions";
			}
		}

	}

	@Override
	public void onNewTweet(Tweet tweet) {
		TweetListFragment fragment = (TweetListFragment) adapterViewPager
				.getItem(POSITION_OF_HOMETIMELINE);
		if (fragment != null) {

			fragment.addNewTweet(tweet);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		ViewPager vpPager = (ViewPager) findViewById(R.id.viewPager);
		//PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.pager_header);
		
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.slidingTabStrip);
        tabs.setViewPager(vpPager);
        tabs.setTextColor(getResources().getColor(R.color.TwitterBlue));


		scheduleAlarm();
	}

	public void scheduleAlarm() {
		// Construct an intent that will execute the AlarmReceiver
		Intent intent = new Intent(getApplicationContext(),
				OfflineTweetAlarmReceiver.class);

		// Create a PendingIntent to be triggered when the alarm goes off
		final PendingIntent pIntent = PendingIntent.getBroadcast(this,
				OfflineTweetAlarmReceiver.REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Setup periodic alarm every 10 seconds
		long firstMillis = System.currentTimeMillis(); // first run of alarm is
														// immediate
		int intervalMillis = 10000; // 10 seconds
		AlarmManager alarm = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
				intervalMillis, pIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_compose) {
			Util.onCompose(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
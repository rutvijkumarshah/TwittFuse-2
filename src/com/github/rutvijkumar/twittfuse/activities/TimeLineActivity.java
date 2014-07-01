package com.github.rutvijkumar.twittfuse.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.fragments.DirectMessagesFragments;
import com.github.rutvijkumar.twittfuse.fragments.HomeTimeLineFragment;
import com.github.rutvijkumar.twittfuse.fragments.MentionsFragment;
import com.github.rutvijkumar.twittfuse.fragments.TweetListFragment;
import com.github.rutvijkumar.twittfuse.models.Tweet;

/***
 * Users Home Time Line Activity
 * <p>
 * Activity list all tweets on users timeline in endless paginated manner.
 * </p
 * 
 * @author Rutvijkumar Shah
 * 
 */
public class TimeLineActivity extends BaseFragmentActivity implements
		OnNewTweetListener {

	private MyPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;
	private ViewPager vpPager;
	
	private static final int POSITION_OF_HOMETIMELINE = 0;
	private static final int POSITION_OF_MENTIONS = 1;
	private static final int POSITION_OF_DIRECTMSGS = 2;
	private FragmentManager supportFragmentManager;
		
	@Override
	public void onNewTweet(Tweet tweet) {
		
//		HomeTimeLineFragment fragment = (HomeTimeLineFragment) adapterViewPager
//				.getItem(POSITION_OF_HOMETIMELINE);
//		
		supportFragmentManager = getSupportFragmentManager();
		HomeTimeLineFragment fragment = (HomeTimeLineFragment) supportFragmentManager.findFragmentByTag(makeFragmentName(R.id.viewPager, POSITION_OF_HOMETIMELINE));
		if (fragment != null) {
			fragment.addNewTweet(tweet);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		vpPager = (ViewPager) findViewById(R.id.viewPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		setupSlidingTabs(vpPager);
		Util.scheduleAlarm(this);
	}

	private void setupSlidingTabs(ViewPager vpPager) {
		tabs = (PagerSlidingTabStrip) findViewById(R.id.slidingTabStrip);
        tabs.setViewPager(vpPager);
        tabs.setTextColor(getResources().getColor(R.color.TwitterBlue));

        tabs.setIndicatorColor(getResources().getColor(R.color.TwitterBlue));
        tabs.setDividerColor(getResources().getColor(android.R.color.white));
        tabs.setShouldExpand(true);
        tabs.setAllCaps(true); 
        
	}

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 3;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case POSITION_OF_HOMETIMELINE: 
				return HomeTimeLineFragment.newInstance(position, "Home");
			case POSITION_OF_MENTIONS: 
				return MentionsFragment.newInstance(position, "Mentions");
			case POSITION_OF_DIRECTMSGS:
				return DirectMessagesFragments.newInstance(position, "Mentions");
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == POSITION_OF_HOMETIMELINE) {
				return "       Home";
			} else if (position == POSITION_OF_MENTIONS) { 
				return "Mentions";
			}else{
				return "Messages";
			}
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		composeActionVisibility(true);
		return true;
	}
	
	private static String makeFragmentName(int viewId, int position)
	{
	     return "android:switcher:" + viewId + ":" + position;
	}
}
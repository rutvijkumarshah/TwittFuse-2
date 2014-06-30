package com.github.rutvijkumar.twittfuse.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

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
public class TimeLineActivity extends FragmentActivity implements
		OnNewTweetListener {

	private MyPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;
	private SearchView searchView;
	private static final int POSITION_OF_HOMETIMELINE = 0;
	private static final int POSITION_OF_MENTIONS = 1;
	private static final int POSITION_OF_DIRECTMSGS = 2;
		
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


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int menuItemId=item.getItemId();
		if (menuItemId == R.id.action_compose) {
			Util.onCompose(this);
		}
		if( menuItemId == R.id.action_profileView) {
			Util.onProfileView(this);
		}
		return super.onOptionsItemSelected(item);
	}
	
	

		
	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 3;

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
				return HomeTimeLineFragment.newInstance(position, "Home");
			case POSITION_OF_MENTIONS: // Fragment # 0 - This will show
										// FirstFragment different title
				return MentionsFragment.newInstance(position, "Mentions");

			case POSITION_OF_DIRECTMSGS:
				return DirectMessagesFragments.newInstance(position, "Mentions");
				
			default:
				return null;
			}
		}

		// Returns the page title for the top i;ndicator
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

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tweets_menus, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchView);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
	
		return true;
	}
	
	private void search(String query) {
		Intent searchIntent=new Intent(this,SearchActivity.class);
		searchIntent.putExtra("SEARCH_QUERY", query);
		startActivity(searchIntent);
	}
	private void setupSearchView(final SearchView searchView) {

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				boolean isValidSubmit = false;
				if (query != null && !query.isEmpty()) {
					search(query);
					isValidSubmit = true;
				}

				return isValidSubmit;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

}
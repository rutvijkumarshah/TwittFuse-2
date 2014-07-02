package com.github.rutvijkumar.twittfuse.activities;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.fragments.ProfileDesciptionFragment;
import com.github.rutvijkumar.twittfuse.fragments.ProfileImageViewFragment;
import com.github.rutvijkumar.twittfuse.fragments.UserTimeLineFragment;
import com.github.rutvijkumar.twittfuse.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

//Network Call
public class ProfileViewActivity extends BaseFragmentActivity {

	private boolean isMyProfile=false;
	private User _user=null;
	
	private TextView tweetsCount;
	private TextView followingCount;
	private TextView followersCount;
	private TextView followingTv;
	private TextView followTv;
	
	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	
	private static final String ACTION_FOLLOW="ACTION_FOLLOW";
	private static final String ACTION_UNFOLLOW="ACTION_UNFOLLOW";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_view);
		Intent intent = getIntent();
		isMyProfile=intent.getBooleanExtra("PROFILE_EXTRA_ISMYPROFILE",false);
		User user=(User)intent.getSerializableExtra("PROFILE_EXTRA_USEROBJ");
		
		if(isMyProfile == true || user == null) {
			//this is my profile
			isMyProfile=true;
			
			loadMyInfo();
		}else {
			isMyProfile=false;
			//This is other user's profile
			setupUI(user);
		}
		
	}

	public void showFollowersListView(View v) {
		Util.showFollowersListActivity(_user.getScreenName(), this);
	}
	
	public void showFriendsListView(View v) {
		Util.showFriendsListActivity(_user.getScreenName(), this);
	}
	private void setupUI(User user) {
		_user=user;
		getActionBar().setTitle("@"+user.getScreenName());
		setFragment(user);
		
		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		tweetsCount=(TextView)findViewById(R.id.tweetCount);
		followingCount=(TextView)findViewById(R.id.followingCount);
		followersCount=(TextView)findViewById(R.id.followers_count);
		
		followingTv=(TextView)findViewById(R.id.tvFollowing);
		followTv=(TextView)findViewById(R.id.tvFollow);
		
		
		tweetsCount.setText(Util.formatCount(user.getTweetsCount(),false));
		followingCount.setText(Util.formatCount(user.getFollowingCount(),true));
		followersCount.setText(Util.formatCount(user.getFollowersCount(),true));
		
		followingTv.setOnClickListener(getFollowClickListener());
		followTv.setOnClickListener(getFollowClickListener());
		
		if(!isMyProfile) {
			if(!user.isFollowing()) {
				followingTv.setVisibility(View.INVISIBLE);
				
			}else {
				followTv.setVisibility(View.INVISIBLE);
			}
			
		}else {
			followingTv.setVisibility(View.GONE);
			followTv.setVisibility(View.GONE);
		}
		
	
	}

	private void confirmUnFollow() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Unfollow "+_user.getName());
		builder.setPositiveButton("Unfollow",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int whichButton) {
						if(!Util.isNetworkAvailable(ProfileViewActivity.this)) {
							Util.showNetworkUnavailable(ProfileViewActivity.this);
						}else {
							unFollowUser();
						}

					}

				});
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
		
	}
	private void unFollowUser() {
		client.unFollowUser(_user.getScreenName(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				followingTv.setVisibility(View.INVISIBLE);
				followTv.setVisibility(View.VISIBLE);
			}
		});
	}
	private void followUser() {
		client.followUser(_user.getScreenName(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				followTv.setVisibility(View.INVISIBLE);
				followingTv.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private OnClickListener getFollowClickListener() {
		OnClickListener followClickListener=new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String action=(String)v.getTag();
				if(action.equals(ACTION_FOLLOW)) {
					followUser();
				}else if (action.equals(ACTION_UNFOLLOW)) {
					confirmUnFollow();
				}
			}
			
		};
		return followClickListener;
	}
	
  private void setFragment(User user) {
		
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.tweetListFragment,UserTimeLineFragment.newInstance(0, "Posts", user.getScreenName()));
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}
	
	private void loadMyInfo() {
		client.getMyAccountInfo(new JsonHttpResponseHandler() {
	
			@Override
			public void onSuccess(JSONObject jsonObj) {
				setupUI(User.fromJson(jsonObj));
			}
		});
		
	}

	class TestFragmentAdapter extends FragmentPagerAdapter  implements IconPagerAdapter{

		public TestFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return ProfileImageViewFragment.newInstance(_user);
			} else {
				return ProfileDesciptionFragment.newInstance(_user);
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return 0;
		}


	}

}
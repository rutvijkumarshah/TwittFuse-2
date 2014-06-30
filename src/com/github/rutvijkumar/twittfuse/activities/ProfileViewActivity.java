package com.github.rutvijkumar.twittfuse.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.fragments.UserTimeLineFragment;
import com.github.rutvijkumar.twittfuse.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

//Network Call
public class ProfileViewActivity extends FragmentActivity {

	private boolean isMyProfile=false;
	//private User user=null;
	private TwitterClient client;
	private ImageView profileImg;
	private TextView userName;
	private TextView userScreenName;
	private TextView tweetsCount;
	private TextView followingCount;
	private TextView followersCount;
	private TextView followingORFollow;
	private FrameLayout tweetListFragment;
	
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
			client=TwitterApplication.getRestClient();
			loadMyInfo();
		}else {
			isMyProfile=false;
			//This is other user's profile
			setupUI(user);
		}
		
	}

	
	private void setupUI(User user) {
		Log.d("USER", user.toJSONString());
		getActionBar().setTitle("@"+user.getScreenName());
		setFragment(user);
		profileImg=(ImageView)findViewById(R.id.profileImage);
		userName=(TextView)findViewById(R.id.userName);
		userScreenName=(TextView)findViewById(R.id.userscreenName);
		final RelativeLayout profileInfoLayout=(RelativeLayout)findViewById(R.id.profileInfo);
		
		tweetsCount=(TextView)findViewById(R.id.tweetCount);
		followingCount=(TextView)findViewById(R.id.followingCount);
		followersCount=(TextView)findViewById(R.id.followers_count);
		
		followingORFollow=(TextView)findViewById(R.id.followORFollowing);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(),
				profileImg);
		
		userName.setText(user.getName());
		userScreenName.setText("@"+user.getScreenName());
		
		tweetsCount.setText(Util.formatCount(user.getTweetsCount(),false));
		followingCount.setText(Util.formatCount(user.getFollowingCount(),true));
		followersCount.setText(Util.formatCount(user.getFollowersCount(),true));
		
		if(!isMyProfile) {
			setFollowingOrFollower(followingORFollow,user.isFollowing());
		}
		final String bannerImageUrl=user.getProfileBannerImageUrl();
		if(bannerImageUrl!=null) {
			setBackGround(user, imageLoader, profileInfoLayout,bannerImageUrl);
		}else {
			final String backGroundImageUrl=user.getProfileBackgroundImageUrl();
			if(backGroundImageUrl!=null) {
				setBackGround(user,imageLoader, profileInfoLayout,backGroundImageUrl);
			}
		}
	
	}
	private void setBackGround(User user,ImageLoader imageLoader,final RelativeLayout profileInfoLayout,String url) {
		imageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
				// TODO Auto-generated method stub
				BitmapDrawable bg=new BitmapDrawable(getResources(), bitmap);
				profileInfoLayout.setBackground(bg);
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
	private void setFollowingOrFollower(TextView followingORFollow2,
			boolean following) {
		// TODO Auto-generated method stub
		
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
}
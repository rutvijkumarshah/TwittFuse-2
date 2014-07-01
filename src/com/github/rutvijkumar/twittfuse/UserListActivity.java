package com.github.rutvijkumar.twittfuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.rutvijkumar.twittfuse.fragments.UserListFragment;

public class UserListActivity extends FragmentActivity {

	private boolean isFollowersList;
	private String userScreenName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		Intent intent = getIntent();
		Bundle extras=intent.getExtras();

		isFollowersList=extras.getBoolean("isFollowersList");
		userScreenName=extras.getString("userScreenName");
		UserListFragment fragment = UserListFragment.newInstance(0, "List",userScreenName,isFollowersList );
		String title="Followers";
		if(!isFollowersList) {
			title="Following";
		}
		getActionBar().setTitle(title);
		
		setFragment(fragment);
	}
	
	private void setFragment(UserListFragment fragment) {
		
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.userListFragment,fragment);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}
}

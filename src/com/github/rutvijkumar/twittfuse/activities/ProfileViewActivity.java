package com.github.rutvijkumar.twittfuse.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.models.User;

public class ProfileViewActivity extends Activity {

	private boolean isMyProfile=false;
	private User user=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_view);
		Intent intent = getIntent();
		isMyProfile=intent.getBooleanExtra("PROFILE_EXTRA_ISMYPROFILE",false);
		user=(User)intent.getSerializableExtra("PROFILE_EXTRA_USEROBJ");
	}
}

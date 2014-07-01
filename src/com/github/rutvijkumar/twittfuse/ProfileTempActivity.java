package com.github.rutvijkumar.twittfuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.github.rutvijkumar.twittfuse.activities.BaseFragmentActivity;
import com.github.rutvijkumar.twittfuse.fragments.ProfileDesciptionFragment;
import com.github.rutvijkumar.twittfuse.fragments.ProfileImageViewFragment;
import com.github.rutvijkumar.twittfuse.models.User;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

//Network Call
public class ProfileTempActivity extends BaseFragmentActivity {

	private User _user = null;
	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_temp);
		Intent intent = getIntent();
		_user = (User) intent.getSerializableExtra("PROFILE_EXTRA_USEROBJ");

		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

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
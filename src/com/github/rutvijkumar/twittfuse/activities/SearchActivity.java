package com.github.rutvijkumar.twittfuse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.fragments.SearchTweetsFragment;

public class SearchActivity extends BaseFragmentActivity {

	private String searchKeyword;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Intent intent = getIntent();
		searchKeyword=intent.getExtras().getString("SEARCH_QUERY");
		SearchTweetsFragment fragment = SearchTweetsFragment.newInstance(0, "Search", searchKeyword);
		setFragment(fragment);
	}
	
	private void setFragment(SearchTweetsFragment fragment) {
		
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.searchTweetsFragment,fragment);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}
}

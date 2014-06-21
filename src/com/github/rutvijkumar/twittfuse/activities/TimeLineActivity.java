package com.github.rutvijkumar.twittfuse.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.adapters.TweetArrayAdapter;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimeLineActivity extends Activity {

	private TwitterClient client;
	private ArrayList<Tweet> tweets=new ArrayList<Tweet>();
	private TweetArrayAdapter adapter;
	private ListView tweetsListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		client=TwitterApplication.getRestClient();
		populateTimeLine();
		tweetsListView=(ListView)findViewById(R.id.lvTweets);
		adapter=new TweetArrayAdapter(this, tweets);
		tweetsListView.setAdapter(adapter);
	}
	
	public void populateTimeLine() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable e, String s) {
				
			}
			
			@Override
			public void onSuccess(JSONArray jsArray) {
				adapter.addAll(Tweet.fromJSONArray(jsArray));
				Toast.makeText(TimeLineActivity.this, "Size:"+adapter.getCount(), Toast.LENGTH_LONG).show();
			}
		},Util.getMaxSinceId(tweets));
	}
}

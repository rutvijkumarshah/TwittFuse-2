package com.github.rutvijkumar.twittfuse.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.adapters.TweetArrayAdapter;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.helpers.EndlessScrollListener;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.services.OfflineTweetAlarmReceiver;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

/***
 * Users Home Time Line Activity
 * <p>
 *  	Activity list all tweets on users timeline in endless paginated manner.
 * </p>
 * @author Rutvijkumar Shah
 *
 */
public class TimeLineActivity extends FragmentActivity implements OnNewTweetListener{

	private TwitterClient client;
	private ArrayList<Tweet> tweets=new ArrayList<Tweet>();
	private TweetArrayAdapter adapter;
	private eu.erikw.PullToRefreshListView tweetsListView;
	private boolean isAlarmScheduled=false;
	
	@Override
	public void onNewTweet(Tweet tweet) {
		adapter.insert(tweet, 0);
		adapter.notifyDataSetChanged();
		tweetsListView.setSelection(0);
	}
	
	private void setupIintialViews() {
		tweetsListView=(eu.erikw.PullToRefreshListView)findViewById(R.id.lvTweets);
		adapter=new TweetArrayAdapter(this, tweets);
		tweetsListView.setAdapter(adapter);
		tweetsListView.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
		             populateTimeLine(totalItemsCount);
	        	}
	        
	        });
		tweetsListView.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                populateTimeLine();
	            }
	        });
		
		

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_time_line);
		client=TwitterApplication.getRestClient();
		setupIintialViews();//Setting up listeners,paginations,adapters
		populateTimeLine();
		
	  }

	  public void scheduleAlarm() {
	    // Construct an intent that will execute the AlarmReceiver
	    Intent intent = new Intent(getApplicationContext(), OfflineTweetAlarmReceiver.class);
	    
	    // Create a PendingIntent to be triggered when the alarm goes off
	    final PendingIntent pIntent = PendingIntent.getBroadcast(this, OfflineTweetAlarmReceiver.REQUEST_CODE,
	        intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    // Setup periodic alarm every 5 seconds
	    long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
	    int intervalMillis = 5000; // 5 seconds
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
	  }
	
	private void populateTimeLine() {
		populateTimeLine(-1);
	}
	
	private void populateTimeLine(int totalItemsCount) {
		Util.showProgressBar(this);
		if(Util.isNetworkAvailable(this) ) {
			
			if(totalItemsCount >0) {
				Tweet oldestTweet = (Tweet) tweetsListView.getItemAtPosition(totalItemsCount-1);
				if(oldestTweet!=null) {
					//When getting older tweets
					client.getHomeTimeline(new ResponseHandler(),oldestTweet.getUid()-1);
					
				}
				
			}else {
				//When refresh to Pull or Getting Tweets first time after launch of app
				Tweet.deleteAll();
				adapter.clear();
				adapter.notifyDataSetInvalidated();
				client.getHomeTimeline(new ResponseHandler());
			}
		}else {
			Util.showNetworkUnavailable(this);
			//When Network is not available load all tweets from DB
			adapter.clear();
			adapter.notifyDataSetInvalidated();
			List<Tweet> dbTweeets = Tweet.findAll();
			adapter.addAll(dbTweeets);
			tweetsListView.onRefreshComplete();
			Util.hideProgressBar(this);
		}
		
		
	}
	
	/***
	 * Response Handler for TimeLine Activity
	 * @author Rutvijkumar Shah
	 *
	 */
	class ResponseHandler extends JsonHttpResponseHandler {
		ResponseHandler(){
			
		}
		
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONArray response) {
//			onSuccess(response);
//		}
//		
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				String responseString, Throwable throwable) {
//			// TODO Auto-generated method stub
//			onFailure(throwable, responseString);
//		}
		
		@Override
		public void onFailure(Throwable e, String s) {
			Util.hideProgressBar(TimeLineActivity.this);
		}
		
		@Override
		public void onSuccess(JSONArray jsArray) {
			ArrayList<Tweet> tweets =Tweet.fromJSONArray(jsArray);
			for (Tweet tweet : tweets) {
				tweet.persist();
				adapter.add(tweet);
			}
			tweetsListView.onRefreshComplete();
			Util.hideProgressBar(TimeLineActivity.this);
			
			/****
			 * Scheduling Alarm only after getting first fetch from network
			 */
			if(!isAlarmScheduled) {
				scheduleAlarm();
				isAlarmScheduled=false;
			}
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_compose) {
			Util.onCompose(this);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tweets_menus, menu);
		MenuItem composeItem = menu.findItem(R.id.action_compose);
		
		return true;
	}
}
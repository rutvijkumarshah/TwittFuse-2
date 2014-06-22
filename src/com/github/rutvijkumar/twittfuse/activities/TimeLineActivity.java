package com.github.rutvijkumar.twittfuse.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.adapters.TweetArrayAdapter;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.helpers.EndlessScrollListener;
import com.github.rutvijkumar.twittfuse.models.Tweet;
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
public class TimeLineActivity extends Activity {

	private TwitterClient client;
	private ArrayList<Tweet> tweets=new ArrayList<Tweet>();
	private TweetArrayAdapter adapter;
	private eu.erikw.PullToRefreshListView tweetsListView;

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
		}
	}	
}
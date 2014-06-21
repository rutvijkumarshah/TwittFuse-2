package com.github.rutvijkumar.twittfuse.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.adapters.TweetArrayAdapter;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.helpers.EndlessScrollListener;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

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
		tweetsListView.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
		             populateTimeLine(totalItemsCount);
	        	}
	        
	        });
	}
	
	private void populateTimeLine() {
		populateTimeLine(-1);
	}
	public void populateTimeLine(int totalItemsCount) {
		if(totalItemsCount >0) {
			Tweet oldestTweet = (Tweet) tweetsListView.getItemAtPosition(totalItemsCount-1);
			client.getHomeTimeline(new ResponseHandler(),oldestTweet.getUid()-1);
		}else {
			client.getHomeTimeline(new ResponseHandler());
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
			
		}
		
		@Override
		public void onSuccess(JSONArray jsArray) {
			adapter.addAll(Tweet.fromJSONArray(jsArray));
		}
	}
	
}

package com.github.rutvijkumar.twittfuse.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change
																				// this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change
																			// this,
																			// base
																			// API
																			// URL
	public static final String REST_CONSUMER_KEY = "YP3F2Wp4cpeF5xZQcOnnQ522x"; // Change
																				// this
	public static final String REST_CONSUMER_SECRET = "xrCSdAk1dcoYlAiwCZEhEuXRNqsXYBVmIlr7s8FId6Sb24Jfnb"; // Change
																											// this
	public static final String REST_CALLBACK_URL = "oauth://cptwittfuse"; // Change
																			// this
																			// (here
																			// and
																			// in
																			// manifest)
	private static final int MAX_RECORD_COUNT = 30;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getDirectMessages(AsyncHttpResponseHandler handler) {
		getDirectMessages(handler, -1);
	}

	public void getDirectMessages(AsyncHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("direct_messages.json");
		RequestParams params = new RequestParams();
		if (maxId > 0) {
			params.put("max_id", String.valueOf(maxId));
		}
		params.put("count", String.valueOf(MAX_RECORD_COUNT));
		client.get(apiUrl, params, handler);
	}

	
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		getHomeTimeline(handler, -1);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (maxId > 0) {
			params.put("max_id", String.valueOf(maxId));
		}
		params.put("count", String.valueOf(MAX_RECORD_COUNT));
		client.get(apiUrl, params, handler);
	}

	public void markTweetFavorite(boolean isFav, String tweetId,
			AsyncHttpResponseHandler handler) {
		String apiUrl;
		if (isFav) {
			apiUrl = getApiUrl("favorites/create.json");
		} else {
			apiUrl = getApiUrl("favorites/destroy.json");
		}
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		getClient().post(apiUrl, params, handler);

	}

	
	public String[] postTweet(String tweetBody,String replyToTweetId) {
		String apiUrl = getApiUrl("statuses/update.json");

		   OAuthService service = new ServiceBuilder()
           .provider(TwitterApi.class)
           .apiKey(REST_CONSUMER_KEY)
           .apiSecret(REST_CONSUMER_SECRET)
           .build();
		   
		   
		 OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl);
		    request.addBodyParameter("status", tweetBody);
		    service.signRequest(getRequestToken(), request);
		    Response response = request.send();
		    
		    
		    String resonseObj[] =new String[2];
		    resonseObj[0]=String.valueOf(response.getCode());
		    resonseObj[1]=response.getBody();
		    
		    return resonseObj;
	}
	public void getMentions(AsyncHttpResponseHandler handler) {
		getMentions(handler, -1);
	}
	 public void getMentions(AsyncHttpResponseHandler handler,long max_id){
    	 String apiUrl = getApiUrl("statuses/mentions_timeline.json");
     	  RequestParams params = new RequestParams();
     	  if(max_id >0 ){
     	     params.put("max_id", String.valueOf(max_id));
     	  }
     	 params.put("count", String.valueOf(MAX_RECORD_COUNT));
     	  getClient().get(apiUrl, params, handler);
    }
	
	 public void getMyAccountInfo(AsyncHttpResponseHandler handler){
	    	String apiUrl = getApiUrl("account/verify_credentials.json");
	    	getClient().get(apiUrl, handler);
	  }
	 
	 
	 public void getUserTimeLine(AsyncHttpResponseHandler handler) {
		 getUserTimeLine(handler, null, -1);
		 
	 }
	 public void getUserTimeLine(AsyncHttpResponseHandler handler,String screenName) {
		 getUserTimeLine(handler, screenName, -1);

	 }
	 public void getUserTimeLine(AsyncHttpResponseHandler handler,String screenName,long max_id) {
		 String url =getApiUrl("statuses/user_timeline.json");
		 RequestParams params= null;
		 
		 if(max_id > 0 || screenName !=null) {
			 params=new RequestParams();
			 if(max_id > 0) {
				 params.put("max_id", String.valueOf(max_id));
			 }
			 if (screenName !=null) {
				 params.put("screen_name",screenName);
			 }
		 }
		 
		 getClient().get(url, params,handler);
	 }
	 
	public void getUserAccountDetails(String userScreenName,AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("users/show.json");
    	RequestParams params=new RequestParams();
    	params.put("screen_name",userScreenName);
    	getClient().get(apiUrl, handler);
    }
	public void postRT(String tweetId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/retweet/" + tweetId + ".json");
		getClient().post(apiUrl, null, handler);
	}

	public void postTweet(String content, 
			AsyncHttpResponseHandler handler) {
		postTweet(content, null, handler);
	}
	
	public void postTweet(String content, String inReplyTo,
			AsyncHttpResponseHandler handler) {
		String apiUrl;
		RequestParams params = new RequestParams();
		apiUrl = getApiUrl("statuses/update.json");
		params.put("status", content);
		if (inReplyTo != null) {
			params.put("in_reply_to_status_id", inReplyTo);
		}
		getClient().post(apiUrl, params, handler);
	}
	
	public void searchTweets(String query, AsyncHttpResponseHandler handler){
    	searchTweets(query, -1, handler);
    }
	
	public void searchTweets(String query, long maxId, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("search/tweets.json");
   	  RequestParams params = new RequestParams();
   	  params.put("count", String.valueOf(25));
   	  params.put("q", query);
   	  if(maxId >0){
   	     params.put("max_id", String.valueOf(maxId));
   	  }
   	  getClient().get(apiUrl, params, handler);
    }
    
	
	public void followUser(String screenName,AsyncHttpResponseHandler handler) {
		String apiUrl=getApiUrl("friendships/create.json");
		RequestParams params=new RequestParams();
		params.put("screen_name",screenName);
		params.put("follow", "true");
		getClient().post(apiUrl,params,handler);
		
	}
	public void unFollowUser(String screenName,AsyncHttpResponseHandler handler) {
		String apiUrl=getApiUrl("friendships/destroy.json");
		RequestParams params=new RequestParams();
		params.put("screen_name",screenName);
		getClient().post(apiUrl,params,handler);
	}
	private void getUserList(String apiUrl, String screen_handle,
			String cursor, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_handle);
		if (cursor != null && !cursor.equals("")) {
			params.put("cursor", cursor);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserList(boolean isFollowersList, String screen_handle,
			String cursor, AsyncHttpResponseHandler handler) {
		if(isFollowersList) {
			getFollowersList(screen_handle, cursor, handler);
		}else {
			getFriendsList(screen_handle, cursor, handler);
		}
	}

	public void getFriendsList(String screen_handle, String cursor,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		getUserList(apiUrl, screen_handle, cursor, handler);
	}

	public void getFollowersList(String screen_handle, String cursor,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		getUserList(apiUrl, screen_handle, cursor, handler);
	}

	
}
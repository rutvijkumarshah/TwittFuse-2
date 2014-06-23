package com.github.rutvijkumar.twittfuse.api;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
	private static final int MAX_RECORD_COUNT = 20;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
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
	
}
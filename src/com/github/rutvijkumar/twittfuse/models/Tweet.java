/***

The MIT License (MIT)
Copyright © 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package com.github.rutvijkumar.twittfuse.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.rutvijkumar.twittfuse.Util;

public class Tweet {

	private String body;
	private long uid;
	private Date createdAt;
	private User user;
	private Boolean isRetweeted;
	private long reTweetCount;
	private long favouritesCount;

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		final int size = jsonArray.length();
		ArrayList<Tweet> list = new ArrayList<Tweet>(size);
		JSONObject tweetJson = null;

		for (int i = 0; i < size; i++) {
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				continue;
			}
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				list.add(tweet);
			}
		}
		return list;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = Util.getTwitterDate(jsonObject.getString("created_at"));
			if(jsonObject.has("retweeted")) {
				tweet.isRetweeted = jsonObject.getBoolean("retweeted");
			}
			if(jsonObject.has("retweet_count")) {
				tweet.reTweetCount = jsonObject.getLong("retweet_count");
			}
			if(jsonObject.has("favourites_count")) {
				tweet.favouritesCount = jsonObject.getLong("favourites_count");
			}
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();	
		}
		return tweet;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public Boolean getIsRetweeted() {
		return isRetweeted;
	}

	public long getReTweetCount() {
		return reTweetCount;
	}

	public long getFavouritesCount() {
		return favouritesCount;
	}

}

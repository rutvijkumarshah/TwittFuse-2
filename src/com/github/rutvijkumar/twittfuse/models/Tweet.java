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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.github.rutvijkumar.twittfuse.Util;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "body")
	private String body;

	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;

	@Column(name = "createdAt", index = true)
	private Date createdAt;

	@Column(name = "reTweetCount")
	private long reTweetCount;

	@Column(name = "offline")
	private String offlineTweet="N";

	@Column(name = "offline_replyTo")
	private String offlineReplyToTweetId;

	@Column(name = "favouritesCount")
	private long favouritesCount;

	@Column(name = "user")
	private User user;

	@Column(name = "retweeted")
	private Tweet reTweeted;

	@Column(name = "favorited_flag")
	private boolean favorited;

	@Column(name = "retweeted_flag")
	private boolean retweeted;

	/***
	 * convenient method to save Tweet Object
	 * 
	 */
	public long persist() {
		if (reTweeted != null) {
			reTweeted.persist();
		}
		user.save();
		return this.save();
	}

	public static List<Tweet> findAll() {
		return new Select().from(Tweet.class).execute();
	}

	public static void deleteAll() {
		new Delete().from(Tweet.class).execute();
	}

	public static List<Tweet> getAllOfflineTweets(int limit) {

		return new Select().from(Tweet.class)
				.where("offline = ?", "Y")
				.orderBy("createdAt ASC")
				.limit(limit)
				.execute();
	}
	
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
			tweet.createdAt = Util.getTwitterDate(jsonObject
					.getString("created_at"));

			if (jsonObject.has("retweet_count")) {
				tweet.reTweetCount = jsonObject.getLong("retweet_count");
			}
			if (jsonObject.has("favorite_count")) {
				tweet.favouritesCount = jsonObject.getLong("favorite_count");

			}
			if (jsonObject.has("retweeted_status")) {
				tweet.reTweeted = Tweet.fromJSON(jsonObject
						.getJSONObject("retweeted_status"));
			}
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.favorited = jsonObject.getBoolean("favorited");
			tweet.retweeted = jsonObject.getBoolean("retweeted");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}



	public boolean isFavorited() {
		return favorited;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public Tweet getReTweeted() {
		return reTweeted;
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

	public long getReTweetCount() {
		return reTweetCount;
	}

	public void setReTweetCount(long reTweetCount) {
		this.reTweetCount = reTweetCount;
	}

	public void setFavouritesCount(long favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public long getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public String getOfflineReplyToTweetId() {
		return offlineReplyToTweetId;
	}

	public void setOfflineReplyToTweetId(String offlineReplyToTweetId) {
		this.offlineReplyToTweetId = offlineReplyToTweetId;
	}

	public boolean isOfflineTweet() {
		return offlineTweet.equals("Y");
	}

	public void setOfflineTweet(boolean offlineTweet) {
		if(offlineTweet) {
			this.offlineTweet="Y";
		}else {
			this.offlineTweet = "N";
		}
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setReTweeted(Tweet reTweeted) {
		this.reTweeted = reTweeted;
	}

}

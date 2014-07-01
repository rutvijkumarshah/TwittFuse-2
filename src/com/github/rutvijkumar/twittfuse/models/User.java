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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "uid")
	private long uid;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	@Column(name = "screenName")
	private String screenName;

	@Column(name="profileBackgroundImageUrl")
	private String profileBackgroundImageUrl;
	
	@Column(name="profileBannerImageUrl")
	private String profileBannerImageUrl;
	
	
	@Column(name="favCount")
	private long favCount;
	
	@Column(name="followersCount")
	private long followersCount;
	
	@Column(name="followingCount")
	private long followingCount;
	
	@Column(name="following")
	private String following;

	@Column(name="tweetsCount")
	private long tweetsCount;

	@Column(name="profileBackgroundColor")
	private String profileBackgroundColor;
	
	
	@Column(name="description")
	private String description;
	
	public String getProfileBannerImageUrl() {
		return profileBannerImageUrl;
	}

	public String getFollowing() {
		return following;
	}

	public String getDescription() {
		return description;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	public String getName() {
		return name;
	}

	public long getFavCount() {
		return favCount;
	}

	public long getFollowersCount() {
		return followersCount;
	}

	public long getFollowingCount() {
		return followingCount;
	}

	public boolean isFollowing() {
		return following.equals("Y");
	}

	public long getTweetsCount() {
		return tweetsCount;
	}

	public long getUid() {
		return uid;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getScreenName() {
		return screenName;
	}

	public static ArrayList<User> fromJSONArray(JSONArray jsonArray) {
		final int size = jsonArray.length();
		ArrayList<User> list = new ArrayList<User>(size);
		JSONObject userJson = null;

		for (int i = 0; i < size; i++) {
			try {
				userJson = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				continue;
			}
			User user = User.fromJson(userJson);
			if (user != null) {
				list.add(user);
			}
		}
		return list;
	}
	
	public static User fromJson(JSONObject jsonObject) {
		User user=new User();
		
		try {
			user.name=jsonObject.getString("name");
			user.uid=jsonObject.getLong("id");;
			user.profileImageUrl=jsonObject.getString("profile_image_url");
			user.screenName=jsonObject.getString("screen_name");
			user.description = jsonObject.getString("description");
			if(jsonObject.has("profile_background_image_url")) {
				user.profileBackgroundImageUrl=jsonObject.getString("profile_background_image_url");
			}
			if(jsonObject.has("following")) {
				user.following = jsonObject.getBoolean("following") ? "Y" :"N";
			}
			if(jsonObject.has("favourites_count")) {
				user.favCount = jsonObject.getLong("favourites_count");
			}
			if(jsonObject.has("friends_count")) {
				user.followingCount = jsonObject.getLong("friends_count");
			}
			if(jsonObject.has("followers_count")) {
				user.followersCount = jsonObject.getLong("followers_count");
			}
			if(jsonObject.has("statuses_count")) {
				user.tweetsCount=jsonObject.getLong("statuses_count");
			}
			if(jsonObject.has("profile_background_color")) {
				user.profileBackgroundColor=jsonObject.getString("profile_background_color");
			}
			if(jsonObject.has("profile_banner_url")) {
				user.profileBannerImageUrl=jsonObject.getString("profile_banner_url");
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}


	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}
	
}

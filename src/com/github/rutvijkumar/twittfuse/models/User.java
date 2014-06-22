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

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model{

	@Column(name = "name")
	private String name;
	
	@Column(name = "uid")
	private long uid;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	@Column(name = "screenName")
	private String screenName;

	public String getName() {
		return name;
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

	public static User fromJson(JSONObject jsonObject) {
		User user=new User();
		
		try {
			user.name=jsonObject.getString("name");
			user.uid=jsonObject.getLong("id");;
			user.profileImageUrl=jsonObject.getString("profile_image_url");
			user.screenName=jsonObject.getString("screen_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	
}

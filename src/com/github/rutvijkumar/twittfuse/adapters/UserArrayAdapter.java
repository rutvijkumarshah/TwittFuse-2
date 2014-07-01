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

package com.github.rutvijkumar.twittfuse.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.activities.ProfileViewActivity;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserArrayAdapter extends ArrayAdapter<User> {

	
	private Context context;	
	private TwitterClient client;
		
	// View lookup cache
	private static class ViewHolder {
		TextView name;// @+id/tweetUserName
		TextView screenName; // @+id/tweetUserScreenName
		ImageView userProfilePic;// @+id/profileImg
		TextView userBio; // @+id/tweetBodyTxt
		TextView followAction;	
	}
//
	public UserArrayAdapter(Context context, ArrayList<User> tweets) {
		super(context, R.layout.tweet_litem, tweets);
		this.context=context;
		this.client=new TwitterClient(context);
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final User user = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_litem, parent, false);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tweetUserName);
			viewHolder.screenName = (TextView) convertView
					.findViewById(R.id.tweetUserScreenName);
			viewHolder.userBio = (TextView) convertView
					.findViewById(R.id.tv_userBio);
			viewHolder.userProfilePic = (ImageView) convertView
					.findViewById(R.id.profileImg);
			
			viewHolder.followAction = (TextView) convertView
					.findViewById(R.id.profileImg);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		
		
		
		
		viewHolder.userProfilePic.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(),viewHolder.userProfilePic);
		
		viewHolder.name.setText(user.getName());
		viewHolder.screenName.setText("@"+user.getScreenName());
		viewHolder.userProfilePic.setTag(user);
		viewHolder.userBio.setText(user.getDescription());
		
		if(!user.isFollowing()) {
			viewHolder.followAction.setVisibility(View.VISIBLE);
		}else {
			viewHolder.followAction.setVisibility(View.INVISIBLE);
		}
		
		//pullToRefreshListView setOnItem click listerner is not working because of auto link
		convertView.setOnClickListener(new UserOnClickListener(user));
		
		return convertView;
	}
	
	
	class UserOnClickListener implements OnClickListener{

		private User user;
		public UserOnClickListener(User user) {
			this.user=user;
		}
		@Override
		public void onClick(View v) {
			Intent detailsIntent = new Intent(getContext(), ProfileViewActivity.class);
			detailsIntent.putExtra("PROFILE_EXTRA_USEROBJ", user);
			getContext().startActivity(detailsIntent);
			
		}
		
	}
}
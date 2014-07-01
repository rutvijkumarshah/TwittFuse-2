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

package com.github.rutvijkumar.twittfuse.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ProfileImageViewFragment extends Fragment {

	
	private ImageView profileImg;
	private TextView userName;
	private TextView userScreenName;
	private RelativeLayout profileInfoLayout;

	private User user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		user=(User)getArguments().get("EXTRA_USER_OBJ");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_image, container,
				false);
		
		profileImg= (ImageView)v.findViewById(R.id.profileImage);
		userName=(TextView)v.findViewById(R.id.userName);
		userScreenName=(TextView)v.findViewById(R.id.userscreenName);
		profileInfoLayout=(RelativeLayout)v.findViewById(R.id.profileInfo);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
			
		imageLoader.displayImage(user.getProfileImageUrl(),
				profileImg);
	
		userName.setText(user.getName());
		userScreenName.setText("@"+user.getScreenName());
	
		final String bannerImageUrl=user.getProfileBannerImageUrl();
		if(bannerImageUrl!=null) {
			setBackGround(user, imageLoader, profileInfoLayout,bannerImageUrl);
		}else {
			final String backGroundImageUrl=user.getProfileBackgroundImageUrl();
			if(backGroundImageUrl!=null) {
				setBackGround(user,imageLoader, profileInfoLayout,backGroundImageUrl);
			}
		}
		
		return v;

	}
	
	public static ProfileImageViewFragment newInstance(User user) {
		ProfileImageViewFragment fragment = new ProfileImageViewFragment();
	        Bundle args = new Bundle();
	        args.putSerializable("EXTRA_USER_OBJ", user);
	        fragment.setArguments(args);
	        return fragment;
	}
	
	private void setBackGround(User user,ImageLoader imageLoader,final RelativeLayout profileInfoLayout,String url) {
		imageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
				// TODO Auto-generated method stub
				BitmapDrawable bg=new BitmapDrawable(getResources(), bitmap);
				profileInfoLayout.setBackground(bg);
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
}


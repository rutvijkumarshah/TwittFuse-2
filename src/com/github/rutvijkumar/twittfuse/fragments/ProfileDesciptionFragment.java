/***

The MIT License (MIT)
Copyright � 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the �Software�), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

***/

package com.github.rutvijkumar.twittfuse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.models.User;

public class ProfileDesciptionFragment extends Fragment {

	private TextView descriptionTv;;
	private User user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		user=(User)getArguments().get("EXTRA_USER_OBJ");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_desc, container,
				false);
		descriptionTv = (TextView) v
				.findViewById(R.id.userDesc);
		
		descriptionTv.setText(user.getDescription());
		return v;
	}
	
	public static ProfileDesciptionFragment newInstance(User user) {
		ProfileDesciptionFragment fragment = new ProfileDesciptionFragment();
	        Bundle args = new Bundle();
	        args.putSerializable("EXTRA_USER_OBJ", user);
	        fragment.setArguments(args);
	        return fragment;
	}
}

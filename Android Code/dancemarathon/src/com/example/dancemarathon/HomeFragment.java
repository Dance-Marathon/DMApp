package com.example.dancemarathon;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment
{

	public HomeFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		
		setButtonListeners(v);
		return v;
	}
	
	public static HomeFragment newInstance()
	{
		HomeFragment f = new HomeFragment();
		return f;
	}
	
	/**
	 * This method sets the listeners for the home screen's buttons
	 * @param v The view the buttons belong to
	 */
	private void setButtonListeners(View v)
	{
		Button gameButton = (Button) v.findViewById(R.id.game);
		Button websiteButton = (Button) v.findViewById(R.id.website);
		Button donateButton = (Button) v.findViewById(R.id.donate);
		
		setButtonTracker(gameButton);
		setButtonTracker(websiteButton);
		setButtonTracker(donateButton);
	}
	
	/**
	 * This method implements google analytics to track the button clicks
	 * @param b The button to track
	 */
	private void setButtonTracker(final Button b)
	{
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String buttonName = b.getText().toString();
				int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplication());
				if(canTrack == ConnectionResult.SUCCESS)
				{
					//Log.d("Tracking", "SwipeActivity");
					TrackerManager.sendEvent((MyApplication) getActivity().getApplication(), "Button", "Clicked", buttonName);
				}
			}
			
		});
	}
}

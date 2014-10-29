package com.example.dancemarathon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment
{

	//Do not use in ViewPager
	public HomeFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	public static HomeFragment newInstance()
	{
		HomeFragment f = new HomeFragment();
		return f;
	}
}

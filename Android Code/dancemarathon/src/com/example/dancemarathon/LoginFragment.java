package com.example.dancemarathon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class LoginFragment extends Fragment
{

	public LoginFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		v.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				//Hide keyboard from edit text views if user clicks outside of the keyboard
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					    Activity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			}
			
		});
		return v;
	}
	
	public static LoginFragment newInstance()
	{
		LoginFragment lf = new LoginFragment();
		return lf;
	}

}

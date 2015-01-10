package com.example.dancemarathon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkProfile extends ActionBarActivity
{
	Kids kid;
	
	public MtkProfile()
	{
		// Required empty public constructor
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mtk_profile);
		
		//Get user from intent
		Kids kid = getIntent().getExtras().getParcelable("kid");
		this.kid = kid;
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle(kid.getName());
	}
	
	public static MtkProfile newInstance()
	{
		MtkProfile f = new MtkProfile();
		return f;
	}
}

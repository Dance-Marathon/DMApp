package com.uf.dancemarathon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class LoginActivity extends AppCompatActivity
{
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Hide Action Bar
		ActionBar bar = getSupportActionBar();

        int color = getResources().getColor(R.color.action_bar_color);
        ColorDrawable cd = new ColorDrawable();
        cd.setColor(color);
        bar.setBackgroundDrawable(cd);

		//Add login fragment
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.kintera_container, LoginFragment.newInstance()).commit();
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "//LoginActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.LOGIN_ACTIVITY_NAME);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);

        return false; //return false to hide the menu
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

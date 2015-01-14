package com.uf.dancemarathon;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.uf.dancemarathon.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class LoginActivity extends ActionBarActivity
{
	/**
	 * Flag used to pass back user to SwipeActivity.
	 */
	public static int IS_USER_STILL_LOGGED_IN = 5;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Hide Action Bar
		getSupportActionBar().hide();
				
		FragmentManager manager = getSupportFragmentManager();
		
		//We don't want duplicate fragments on top of each other
		if(savedInstanceState != null)
			return;
		
		manager.beginTransaction().add(R.id.kintera_container, LoginFragment.newInstance()).commit();
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "//LoginActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), "//Login Screen");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
	
	//This method handles passing the user object back
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == LoginFragment.IS_USER_STILL_LOGGED_IN)
		{
			if(resultCode == Activity.RESULT_CANCELED)
			{
				setResult(Activity.RESULT_CANCELED, new Intent());
			}
			else if(resultCode == Activity.RESULT_OK)
			{
				setResult(Activity.RESULT_OK, data);
			}
			
			this.finish();
		}
	}
}

package com.example.dancemarathon;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class UserActivity extends ActionBarActivity
{
	KinteraUser user;
	UserLoader loader;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//Get user from intent
		KinteraUser user = getIntent().getExtras().getParcelable("user");
		this.user = user;
		//Log.d("User", user.realName);
		
		//Populate all of the necessary fields
		setFields(user);
		
		//Set result of this activity to the user
		setActivityResult(user);
		
		//Instantiate loader to prevent null
		loader = new UserLoader();
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Fundraising Progress");
		
		int color = getResources().getColor(R.color.dm_orange_primary);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
	}
	
	protected void onStop()
	{
		super.onStop();
		//If a loader exists, cancel its execution
		if(loader != null)
			loader.cancel(true);
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			Log.d("Tracking", "UserActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), "User Screen");
		}
	}
	
	/**
	 * Set all the important data fields for this view
	 * @param user The user
	 */
	private void setFields(final KinteraUser user)
	{
		//Set textviews
		TextView name = (TextView) findViewById(R.id.user_name);
		TextView goal = (TextView) findViewById(R.id.user_goal);
		TextView raised = (TextView) findViewById(R.id.user_raised);
		TextView progress = (TextView) findViewById(R.id.user_progress);
		
		name.setText(user.realName);
		goal.setText("$" + Integer.toString((int) user.fundGoal));
		raised.setText("$" + Integer.toString((int) user.fundRaised));
		int percentRaised = (int) ((user.fundRaised / user.fundGoal) * 100);
		progress.setText(Integer.toString(percentRaised)+ "%");
		
		//Set listener for kintera page button
		Button pageButton = (Button) findViewById(R.id.user_page_button);
		pageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				openKinteraPage(user.pageURL);
			}
		});
	}
	
	/**
	 * Clears the user cache file and exits this activity
	 * @param v The logout button view
	 */
	public void logout(View v)
	{
		this.setResult(RESULT_CANCELED);
		CacheManager.clearCacheFile(this, "user");
		this.finish();
	}
	
	/**
	 * This method sets the result of this activity with the input user
	 * @param user The user to report
	 */
	public void setActivityResult(KinteraUser user)
	{
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putParcelable("user", user);
		i.putExtras(b);
		setResult(RESULT_OK, i);
	}
	
	/**
	 * Refresh the user information
	 * @param username The username to use
	 * @param password The password to use
	 */
	public void refreshUser(String username, String password)
	{
		findViewById(R.id.user_loading_overlay).setVisibility(View.VISIBLE);
		loader = new UserLoader();
		loader.execute(username, password);
	}
	
	/**
	 * Open the user's kintera page in the browser
	 * @param url
	 */
	public void openKinteraPage(String url)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh)
		{
			//Log.d("crendential", user.userName + user.getPassword());
			refreshUser(user.userName, user.getPassword());
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This method shows a toast with the given message
	 * @param message The message to show
	 */
	private void makeToast(String message)
	{
		Toast toast = Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}
	
	
	/**
	 * @author Chris Whitten
	 * This class is responsible for loading all the kintera information about the user.
	 * The onProgressUpdate() method is used to display any error messages as toasts
	 *
	 */
	private class UserLoader extends AsyncTask<String, String, KinteraUser>
	{
		String username="";
		String password="";
		boolean loadSuccessful = false;
		@Override
		protected KinteraUser doInBackground(String... params)
		{	
			//Begin loading work
			KinteraUser user = new KinteraUser();
			URL url;
			try
			{
				//Get username and password from params
				username = params[0];
				password = params[1];
				
				//Set path
				String path = "http://mickmaccallum.com/ian/kintera.php?";
				path += "username=" + username;
				path += "&password=" + password;
				
				//Connect to the webservice
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				//Parse JSON response
				if(conn.getResponseCode() == 200)
				{
					String jsonRep = reader.readLine().trim();
					if(jsonRep.equals("Error"))
						publishProgress("Invalid Credentials");
					else
					{
						JSONObject o = new JSONObject(jsonRep);
						user = parseUserJson(o);
						loadSuccessful = true;
					}
				}
				else
				{
					publishProgress("Sorry, we are currently experiencing server problems!");
				}
				
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Sorry, we are currently experiencing technical problems!");
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Could not load user data! Check internet connection.");
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Sorry, we are currently experiencing technical problems!");
				e.printStackTrace();
			}
			
			return user;
		}
		
		protected void onProgressUpdate(String... params)
		{
			makeToast(params[0]);
		}
		protected void onPostExecute(KinteraUser user)
		{	
			//If the load was successful, refresh the user activity
			if(loadSuccessful)
			{
				//Write user data to cache
				CacheManager.clearCacheFile(UserActivity.this, "user");
				CacheManager.writeObjectToCacheFile(UserActivity.this, user, "user");
				
				//Refresh the page
				setFields(user);
				
				//Set this activity's result
				setActivityResult(user);
				
				UserActivity.this.user = user;
				
				//Clear loading overlay
				findViewById(R.id.user_loading_overlay).setVisibility(View.GONE);
			}
			else
			{
			}
		}
		
		private KinteraUser parseUserJson(JSONObject o) throws JSONException
		{
			//Parse json
			String realName = o.getString("ParticipantName");
			String pageURL = o.getString("PersonalPageUrl");
			double fundGoal = Double.parseDouble(o.getString("PersonalGoal"));
			double fundRaised = Double.parseDouble(o.getString("PersonalRaised"));
			
			KinteraUser user = new KinteraUser(username, password, realName, fundGoal, fundRaised, pageURL);
			return user;
		}
	}
	
}

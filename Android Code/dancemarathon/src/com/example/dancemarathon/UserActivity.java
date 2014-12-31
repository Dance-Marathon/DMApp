package com.example.dancemarathon;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Fundraising Progress");
		
		int color = getResources().getColor(R.color.dm_orange_primary);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
		
		final KinteraUser user = getIntent().getExtras().getParcelable("user");
		//Log.d("User", user.realName);
		
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
	public void logout(View v)
	{
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
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
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

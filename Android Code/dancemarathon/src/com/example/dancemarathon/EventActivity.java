package com.example.dancemarathon;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EventActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		//Get the textviews
		TextView title = (TextView) findViewById(R.id.event_page_title);
		TextView desc = (TextView) findViewById(R.id.event_page_desc);
		TextView stime = (TextView) findViewById(R.id.event_page_stime);
		TextView etime = (TextView) findViewById(R.id.event_page_etime);
		TextView location = (TextView) findViewById(R.id.event_page_loc);
		
		//Set the textviews
		Bundle b = getIntent().getExtras();
		title.setText(b.getString("e_title"));
		desc.setText(b.getString("e_desc"));
		stime.setText(b.getString("e_stime"));
		etime.setText(b.getString("e_etime"));
		location.setText(b.getString("e_loc"));
		
		//Set action bar title
		ActionBar bar = getSupportActionBar();
		bar.setTitle(title.getText() + " details");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
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

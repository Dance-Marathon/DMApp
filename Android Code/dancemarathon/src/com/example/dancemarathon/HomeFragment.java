package com.example.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment
{
	private boolean loadSuccessful;
	private AnnouncementsLoader loader;
	/**
	 * The path to the event webservice on the server
	 */
	private static final String announcementWebServicePath = "http://floridadm.org/app/announcements.php";
	
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
		
		Typeface header_font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AGBookRouCFFReg.otf");
		Typeface announcement_font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AGBookRouCFFBol.otf");
		Typeface button_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AvenirLTStd-Light.ttf");
		
		TextView header = (TextView) v.findViewById(R.id.header_text);
		TextView announcement_header = (TextView) v.findViewById(R.id.announcements_title);
		TextView game_text = (TextView) v.findViewById(R.id.game);
		TextView web_text = (TextView) v.findViewById(R.id.website);
		TextView donate = (TextView) v.findViewById(R.id.donate);
		
		header.setTypeface(header_font);
		announcement_header.setTypeface(announcement_font);
		game_text.setTypeface(button_font);
		web_text.setTypeface(button_font);
		donate.setTypeface(button_font);
		
		setButtonListeners(v);
		loader = new AnnouncementsLoader();
		loader.execute();
		
		return v;
	}
	
	public void onStop()
	{
		super.onStop();
		if(loader != null)
			loader.cancel(true);
	}
	
	public static HomeFragment newInstance()
	{
		HomeFragment f = new HomeFragment();
		return f;
	}
	
	/**
	 * This method displays an error toast
	 */
	private void displayErrorToast()
	{
		Toast toast = Toast.makeText(getActivity(), "Could not load Announcements", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * This class is responsible for loading the events. It is necessary because Android
	 * does not allow you to have loading operations on the same thread as the UI.
	 */
	private class AnnouncementsLoader extends AsyncTask<Void, Double, ArrayList<Announcement>>
	{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		//This method will perform the request to the web service and try to obtain the events
		@Override
		protected ArrayList<Announcement> doInBackground(Void... params)
		{
			ArrayList<Announcement> announcements = new ArrayList<Announcement>();
			try
			{	
				URL url = new URL(announcementWebServicePath); //The path to the webservice 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				//Parse JSON response
				String announcementsJSON = reader.readLine();
				// Log.d("json", eventsJSON);
				JSONArray arr = new JSONArray(announcementsJSON);
				announcements = parseAnnouncementsJSON(arr);
				
				//Write data to cache
				CacheManager.writeObjectToCacheFile(getActivity(), announcements, "announcements");
				
				//Set success flag to true
				loadSuccessful = true;
				
				
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				loadSuccessful = false;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				loadSuccessful = false;
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				loadSuccessful = false;
			}
			
			return announcements;
		}
		
			
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		//This method will update the UI after the load is finished.
		protected void onPostExecute(ArrayList<Announcement> announcements)
		{
			final ListView list = (ListView) getView().findViewById(R.id.announcements_list);
			
			if(loadSuccessful)
			{
				AnnouncementsAdapter adapter = new AnnouncementsAdapter(getActivity(), announcements);
				list.setAdapter(adapter);
				list.setClickable(false);
			}
			else
			{
				displayErrorToast();
			}
		}
		
		/**
		 * @param obj The JSON object containing the events
		 * @return An arraylist of events
		 * @throws JSONException if parse fails
		 */
		protected ArrayList<Announcement> parseAnnouncementsJSON(JSONArray arr) throws JSONException
		{
			ArrayList<Announcement> announcements = new ArrayList<Announcement>();
			for(int i = 0; i < arr.length(); i++)
			{
				String text = arr.getJSONObject(i).getString("text").trim();
				String date = arr.getJSONObject(i).getString("date").trim();
				
				try
				{
					Announcement a = new Announcement(text, date, "yyyy-MM-dd HH:mm:ss");
					announcements.add(a);
				} catch (ParseException e)
				{
					// Log.d("Announcements Parsing", "Failed to parse announcement" + text);
				}
			}
			
			if(announcements.size() <= 0)
				loadSuccessful = false; //Loading nothing does not qualify as a "successful" load operation
			return announcements; 
		}
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

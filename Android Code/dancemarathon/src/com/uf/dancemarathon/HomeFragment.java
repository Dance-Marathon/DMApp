package com.uf.dancemarathon;

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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
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
import com.uf.dancemarathon.FontSetter.fontName;


/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment
{
	private boolean loadSuccessful;
	private AnnouncementsLoader loader;
	
	//Website Paths that will be used if config file read fails//
	private String gameLink = "http://www.google.com";
	private String websiteLink = "http://www.floridadm.org/";
	private String donateLink = "http://floridadm.kintera.org/faf/search/searchParticipants.asp?ievent=1114670&amp;lis=1&amp;kntae1114670=15F87DA40F9142E489120152BF028EB2";
	
	private int second_dm = 0;
	private int minute_dm = 0;
	private int hour_dm = 12;
	private int day_dm = 14;
	private int month_dm = 3;
	private int year_dm = 2015;
	
    private TextView text_days_h;
    private TextView text_days_t;
    private TextView text_days_o;
    private TextView text_hours_t;
    private TextView text_hours_o;
    private TextView text_minutes_t;
    private TextView text_minutes_o;
    private TextView text_seconds_t;
    private TextView text_seconds_o;
	
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

		set_timer_DM(v);
		
		TextView header_text = (TextView) v.findViewById(R.id.header_text);
		TextView announcement_header = (TextView) v.findViewById(R.id.announcements_title);
		TextView game_text = (TextView) v.findViewById(R.id.game);
		TextView web_text = (TextView) v.findViewById(R.id.website);
		TextView donate = (TextView) v.findViewById(R.id.donate);
		
		FontSetter.setFont(getActivity(), fontName.AGBReg, header_text, game_text, web_text, donate);
		FontSetter.setFont(getActivity(), fontName.AGBBol, announcement_header);
		
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
	 * This method sets the listeners for the home screen's buttons
	 * @param v The view the buttons belong to
	 */
	private void setButtonListeners(View v)
	{
		final Button gameButton = (Button) v.findViewById(R.id.game);
		final Button websiteButton = (Button) v.findViewById(R.id.website);
		final Button donateButton = (Button) v.findViewById(R.id.donate);
		
		gameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendButtonHit(gameButton);
				openLink(gameButton);
			}
		});
		
		websiteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendButtonHit(websiteButton);
				openLink(websiteButton);
			}
		});
		
		donateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendButtonHit(donateButton);
				openLink(donateButton);
			}
		});
	}
	
	/**
	 * This method implements google analytics to track the button clicks
	 * @param b The button to track
	 */
	private void sendButtonHit(final Button b)
	{
		// TODO Auto-generated method stub
		String buttonName = b.getText().toString();
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "SwipeActivity");
			TrackerManager.sendEvent((MyApplication) getActivity().getApplication(), "Button", "Clicked", buttonName);
		}
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
				String path = new ConfigFileReader(getActivity()).getSetting("announcementsPath");
				URL url = new URL(path); //The path to the webservice 
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
				//e.printStackTrace();
				loadSuccessful = false;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
				loadSuccessful = false;
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
	 * Called by the buttons to open browser webpages.
	 * @param view The button which called this method
	 */
	public void openLink(View view)
	{
		//Try to get settings
		try {
			ConfigFileReader cReader = new ConfigFileReader(getActivity());
			gameLink = cReader.getSetting("gamePath");
			websiteLink = cReader.getSetting("websitePath");
			donateLink = cReader.getSetting("donatePath");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Open the links
		int id = view.getId();
		if(id == R.id.game)
			openWebsite(gameLink);
		else if(id == R.id.website)
			openWebsite(websiteLink);
		else if(id == R.id.donate)
			openWebsite(donateLink);
		else
		{
		}
			
	}
	
	public void openWebsite(String link)
	{
		Log.d("link", link);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(intent);
	}

	public void set_timer_DM(View v)
	{	
		
		Time timerSet = new Time(Time.getCurrentTimezone());
        timerSet.set(second_dm, minute_dm, hour_dm, day_dm, month_dm, year_dm); //day month year
        timerSet.normalize(true);
        long dmMillis = timerSet.toMillis(true);

        Time TimeNow = new Time(Time.getCurrentTimezone());
        TimeNow.setToNow(); // set the date to Current Time
        TimeNow.normalize(true);
        long nowMillis = TimeNow.toMillis(true);

        long milliDiff= dmMillis - nowMillis; //subtract current from future to set the time remaining

        text_days_h = (TextView) v.findViewById(R.id.days_hundreds);
        text_days_t = (TextView) v.findViewById(R.id.days_tens);
        text_days_o = (TextView) v.findViewById(R.id.days_ones);
        text_hours_t = (TextView) v.findViewById(R.id.hours_tens);
        text_hours_o = (TextView) v.findViewById(R.id.hours_ones);
        text_minutes_t = (TextView) v.findViewById(R.id.minutes_tens);
        text_minutes_o = (TextView) v.findViewById(R.id.minutes_ones);
        text_seconds_t = (TextView) v.findViewById(R.id.seconds_tens);
        text_seconds_o = (TextView) v.findViewById(R.id.seconds_ones);
        
        new CountDownTimer(milliDiff, 1000)
        {
        	@Override	
            public void onTick(long millisUntilFinished)
            {
            	
                // decompose difference into days, hours, minutes and seconds 
                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                // Filter time
                int days_hundreds = days % 100;
                int days_tens = (days - days_hundreds * 100) % 10;
                int days_ones = (days - days_hundreds * 100 - days_tens * 10);
                
                int hours_tens = hours % 10;
                int hours_ones = hours - hours % 10;
                
                int minutes_tens = minutes % 10;
                int minutes_ones = minutes - minutes_tens * 10;
                
                int seconds_tens = seconds % 10;
                int seconds_ones = seconds - seconds_tens * 10;

                
                text_days_h.setText(Integer.toString(days_hundreds));
                text_days_t.setText(Integer.toString(days_tens));
                text_days_o.setText(Integer.toString(days_ones));
                text_hours_t.setText(Integer.toString(hours_tens));
                text_hours_o.setText(Integer.toString(hours_ones));
                text_minutes_t.setText(Integer.toString(minutes_tens));
                text_minutes_o.setText(Integer.toString(minutes_ones));
                text_seconds_t.setText(Integer.toString(seconds_tens));
                text_seconds_o.setText(Integer.toString(seconds_ones));
            }
            
            @Override
            public void onFinish()
            {
            	text_days_h.setText("S");
                text_days_t.setText("T");
                text_days_o.setText("A");
                text_hours_t.setText("N");
                text_hours_o.setText("D");
                text_minutes_t.setText("↑");
                text_minutes_o.setText("4");
                text_seconds_t.setText("THE");
                text_seconds_o.setText("KIDS!");
            }
        };
	}
}

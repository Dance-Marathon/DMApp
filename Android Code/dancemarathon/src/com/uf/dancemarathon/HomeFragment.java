package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private Context c;
	private AnnouncementsLoader loader;
	
	//Website Paths that will be used if config file read fails//
	private String gameLink = "http://www.google.com";
	private String websiteLink = "http://www.floridadm.org/";
	private String donateLink = "http://floridadm.kintera.org/faf/search/searchParticipants.asp?ievent=1114670&amp;lis=1&amp;kntae1114670=15F87DA40F9142E489120152BF028EB2";

	// TextViews to be used in the countdown
    private TextView text_days_h;
    private TextView text_days_t;
    private TextView text_days_o;
    private TextView text_colon_1;
    private TextView text_hours_t;
    private TextView text_hours_o;
    private TextView text_colon_2;
    private TextView text_minutes_t;
    private TextView text_minutes_o;
    private TextView text_colon_3;
    private TextView text_seconds_t;
    private TextView text_seconds_o;
    private TextView countdown_text;
    
    private ImageView bannerImage;
    private RelativeLayout countdownView;

    
	public HomeFragment()
	{
		// Required empty public constructor
	}

	public static HomeFragment newInstance(Context c)
	{
		HomeFragment f = new HomeFragment();
		f.c = c;
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_home, container, false);

		//Get textviews and set fonts
		TextView header_text = (TextView) v.findViewById(R.id.header_text);
		TextView announcement_header = (TextView) v.findViewById(R.id.announcements_title);
		TextView game_text = (TextView) v.findViewById(R.id.game);
		TextView web_text = (TextView) v.findViewById(R.id.website);
		TextView donate = (TextView) v.findViewById(R.id.donate);
		
		FontSetter.setFont(getActivity(), fontName.AGBReg, header_text, game_text, web_text, donate);
		FontSetter.setFont(getActivity(), fontName.AGBBol, announcement_header);
		
		//Init banner stuff
		bannerImage = (ImageView) v.findViewById(R.id.dance_marathon_header_image);
		countdownView = (RelativeLayout) v.findViewById(R.id.countdown_container);
		
		//Set button listeners
		setButtonListeners(v);
		
		//Try to read data from cache
		 Object o = CacheManager.readObjectFromCacheFile(c , "announcements");
		 //If failed, force update
		if(o == null)
		{
			loader = new AnnouncementsLoader();
			loader.execute();
		}
		//Else show cache events
		else
		{
			ArrayList<Announcement> ments = (ArrayList<Announcement>) o;
			//List must be greater than zero to show cache data
			if(ments.size() > 0)
				 showAnnouncements(ments, v);
			else
			 {
				loader = new AnnouncementsLoader();
				loader.execute();
			 }
		}
		
		return v;
	}
	
	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		set_timer_DM(getView());
	}
	
	public void onStop()
	{
		super.onStop();
		if(loader != null)
			loader.cancel(true);
	}

	
	/**
	 * Update the announcements listview with the input arraylist
	 * @param ments The new announcements
	 * @param v The view containing the listview
	 */
	private void showAnnouncements(ArrayList<Announcement> ments, View v)
	{
		final ListView list = (ListView) v.findViewById(R.id.announcements_list);
		AnnouncementsAdapter adapter = new AnnouncementsAdapter(getActivity(),ments);
		list.setAdapter(adapter);
		list.setClickable(false);
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
	
	
	//Button Handling//
	/**
	 * This method sets the listeners for the home screen's buttons
	 * @param v The view the buttons belong to
	 */
	private void setButtonListeners(View v)
	{
		final Button gameButton = (Button) v.findViewById(R.id.game);
		final Button websiteButton = (Button) v.findViewById(R.id.website);
		final Button donateButton = (Button) v.findViewById(R.id.donate);
		final Button flipButton = (Button) v.findViewById(R.id.banner_flip_button);
		
		gameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendButtonHit(gameButton);
				//Start game activity
				Intent intent = new Intent(getActivity(), GameActivity.class);
				startActivity(intent);
				/*Toast toast = Toast.makeText(getActivity(), "Game coming soon!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();*/
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
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				sendButtonHit(donateButton);
				openLink(donateButton);
			}
		});
		
		flipButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bannerImage.getVisibility() == View.VISIBLE)
				{
					bannerImage.setVisibility(View.GONE);
					countdownView.setVisibility(View.VISIBLE);
					countdown_text.setVisibility(View.VISIBLE);
					flipButton.bringToFront();
				}
				else
				{
					bannerImage.setVisibility(View.VISIBLE);
					countdownView.setVisibility(View.GONE);
					countdown_text.setVisibility(View.GONE);
				}
			}
			
		});
		
		flipButton.bringToFront();
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

	//-----------------//
	
	/**
	 * This class is responsible for loading the events. It is necessary because Android
	 * does not allow you to have loading operations on the same thread as the UI.
	 */
	private class AnnouncementsLoader extends AsyncTask<Void, Double, ArrayList<Announcement>>
	{
		private boolean loadSuccessful;
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
			if(loadSuccessful)
				showAnnouncements(announcements, getView());
			else
				displayErrorToast();
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
					if(a.hasOccurred())
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
	
	public void openWebsite(String link)
	{
		Log.d("link", link);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(intent);
	}

	public void set_timer_DM(final View v)
	{	
        Date date = new Date();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
			date = df.parse("2015-03-14 12:02:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        long milliDiff = date.getTime() - Calendar.getInstance(Locale.US).getTimeInMillis(); 

        text_days_h = (TextView) v.findViewById(R.id.days_hundreds);
        text_days_t = (TextView) v.findViewById(R.id.days_tens);
        text_days_o = (TextView) v.findViewById(R.id.days_ones);
        text_colon_1 = (TextView) v.findViewById(R.id.colon_1);
        text_hours_t = (TextView) v.findViewById(R.id.hours_tens);
        text_hours_o = (TextView) v.findViewById(R.id.hours_ones);
        text_colon_2 = (TextView) v.findViewById(R.id.colon_2);
        text_minutes_t = (TextView) v.findViewById(R.id.minutes_tens);
        text_minutes_o = (TextView) v.findViewById(R.id.minutes_ones);
        text_colon_3 = (TextView) v.findViewById(R.id.colon_3);
        text_seconds_t = (TextView) v.findViewById(R.id.seconds_tens);
        text_seconds_o = (TextView) v.findViewById(R.id.seconds_ones);
        countdown_text = (TextView) v.findViewById(R.id.countdown_text);
        
        countdown_text.setText("Time Until Next Dance Marathon" );
        
        new CountDownTimer(milliDiff, 1000)
        {
        	@Override	
            public void onTick(long millisUntilFinished)
            {
            	Calendar cal = Calendar.getInstance(Locale.US);
            	cal.setTimeInMillis(millisUntilFinished);
            	
            	int time_left = (int) millisUntilFinished / 1000;
            	
            	int days = time_left / 86400;
            	int hours = (time_left - days * 86400) / 3600;
            	int minutes = cal.get(Calendar.MINUTE);
            	int seconds = cal.get(Calendar.SECOND);
            	
                // Filter time
                int days_hundreds = days / 100;
                int days_tens = (days - days_hundreds * 100) / 10;
                int days_ones = (days - days_hundreds * 100 - days_tens * 10);
                
                int hours_tens = hours / 10;
                int hours_ones = hours - hours_tens * 10;
                
                int minutes_tens = minutes / 10;
                int minutes_ones = minutes - minutes_tens * 10;
                
                int seconds_tens = seconds / 10;
                int seconds_ones = seconds - seconds_tens * 10;

                
                text_days_h.setText(Integer.toString(days_hundreds));
                text_days_t.setText(Integer.toString(days_tens));
                text_days_o.setText(Integer.toString(days_ones));
                text_colon_1.setText(":");
                text_hours_t.setText(Integer.toString(hours_tens));
                text_hours_o.setText(Integer.toString(hours_ones));
                text_colon_2.setText(":");
                text_minutes_t.setText(Integer.toString(minutes_tens));
                text_minutes_o.setText(Integer.toString(minutes_ones));
                text_colon_3.setText(":");
                text_seconds_t.setText(Integer.toString(seconds_tens));
                text_seconds_o.setText(Integer.toString(seconds_ones));
            }
            
            @Override
            public void onFinish()
            {
            	set_timer_stand(v);
            }
        }.start();
	}
	
	public void set_timer_stand(View v)
	{	
        Date date_start = new Date();
		Date date_end = new Date();
        
        SimpleDateFormat df_start = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat df_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
			date_start = df_start.parse("2015-03-14 12:00:00");
        	date_end = df_end.parse("2015-03-15 14:12:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        long milliDiff = date_end.getTime() - date_start.getTime(); 

        text_days_h = (TextView) v.findViewById(R.id.days_hundreds);
        text_days_t = (TextView) v.findViewById(R.id.days_tens);
        text_days_o = (TextView) v.findViewById(R.id.days_ones);
        text_colon_1 = (TextView) v.findViewById(R.id.colon_1);
        text_hours_t = (TextView) v.findViewById(R.id.hours_tens);
        text_hours_o = (TextView) v.findViewById(R.id.hours_ones);
        text_colon_2 = (TextView) v.findViewById(R.id.colon_2);
        text_minutes_t = (TextView) v.findViewById(R.id.minutes_tens);
        text_minutes_o = (TextView) v.findViewById(R.id.minutes_ones);
        text_colon_3 = (TextView) v.findViewById(R.id.colon_3);
        text_seconds_t = (TextView) v.findViewById(R.id.seconds_tens);
        text_seconds_o = (TextView) v.findViewById(R.id.seconds_ones);
        countdown_text = (TextView) v.findViewById(R.id.countdown_text);
        
        countdown_text.setText("Standing Time Remaining" );
        
        new CountDownTimer(milliDiff, 1000)
        {
        	@Override	
            public void onTick(long millisUntilFinished)
            {
            	Calendar cal = Calendar.getInstance(Locale.US);
            	cal.setTimeInMillis(millisUntilFinished);
            	
            	int time_left = (int) millisUntilFinished / 1000;
            	
            	int days = time_left / 86400;
            	int hours = (time_left - days * 86400) / 3600;
            	int minutes = cal.get(Calendar.MINUTE);
            	int seconds = cal.get(Calendar.SECOND);
            	
                // Filter time
                int days_hundreds = days / 100;
                int days_tens = (days - days_hundreds * 100) / 10;
                int days_ones = (days - days_hundreds * 100 - days_tens * 10);
                
                int hours_tens = hours / 10;
                int hours_ones = hours - hours_tens * 10;
                
                int minutes_tens = minutes / 10;
                int minutes_ones = minutes - minutes_tens * 10;
                
                int seconds_tens = seconds / 10;
                int seconds_ones = seconds - seconds_tens * 10;

                
                text_days_h.setText(Integer.toString(days_hundreds));
                text_days_t.setText(Integer.toString(days_tens));
                text_days_o.setText(Integer.toString(days_ones));
                text_colon_1.setText(":");
                text_hours_t.setText(Integer.toString(hours_tens));
                text_hours_o.setText(Integer.toString(hours_ones));
                text_colon_2.setText(":");
                text_minutes_t.setText(Integer.toString(minutes_tens));
                text_minutes_o.setText(Integer.toString(minutes_ones));
                text_colon_3.setText(":");
                text_seconds_t.setText(Integer.toString(seconds_tens));
                text_seconds_o.setText(Integer.toString(seconds_ones));
                
                
            }
            
            @Override
            public void onFinish()
            {
            	text_days_h.setText("S");
                text_days_t.setText("I");
                text_days_o.setText("T");
                text_colon_1.setText(" ");
                text_hours_t.setText("D");
                text_hours_o.setText("O");
                text_colon_2.setText("W");
                text_minutes_t.setText("N");
                text_minutes_o.setText("!");
                text_colon_3.setText("!");
                text_seconds_t.setText("!");
                text_seconds_o.setText("!");
            }
        }.start();
	}
}

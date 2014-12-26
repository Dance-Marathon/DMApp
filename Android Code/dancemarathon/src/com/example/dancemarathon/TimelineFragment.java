package com.example.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A {@link Fragment} subclass which is responsible for displaying the timeline information. It uses
 * an {@link AsyncTask} to load the data and then it updates the UI accordingly.
 * 
 */
public class TimelineFragment extends Fragment
{
	/**
	 * The list of Events
	 */
	private ArrayList<Event> events;
	/**
	 * Flag stating whether or not the load operation was successful
	 */
	private boolean loadSuccessful;
	/**
	 * The loader which performs the async load operation.
	 */
	private EventLoader loader;

	public TimelineFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Reset the loader so we can do another load operation.
	 * An instance of async task may only be executed once
	 * so we re-instantiate the loader.
	 */
	public void resetLoader()
	{
		loader = new EventLoader();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_timeline, container, false);
	}
	
	/**
	 * This method is necessary because an empty, no argument constructor must be provided
	 * for a fragment in Android
	 * @return A new instance of timeline fragment that is executing the load operation
	 */
	public static TimelineFragment newInstance()
	{
		TimelineFragment f = new TimelineFragment();
		f.loadSuccessful = false;
		f.resetLoader();
		f.loader.execute(); //Perform the load operation
		return f;
	}
	
	/**
	 * @return the events
	 */
	public ArrayList<Event> getEvents()
	{
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(ArrayList<Event> events)
	{
		this.events = events;
	}

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	//Needed to override this method to cancel the async task if this fragment is stopped
	public void onStop()
	{
		super.onStop();
		loader.cancel(true);
	}
	
	/**
	 * This class is responsible for loading the events. It is necessary because Android
	 * does not allow you to have loading operations on the same thread as the UI.
	 */
	private class EventLoader extends AsyncTask<Void, Double, ArrayList<Event>>
	{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		//This method will perform the request to the web service and try to obtain the events
		@Override
		protected ArrayList<Event> doInBackground(Void... params)
		{
			ArrayList<Event> events = new ArrayList<Event>();
			try
			{	
				URL url = new URL("http://104.236.1.77/app/events.php"); //The path to the webservice 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				//Parse JSON response
				String eventsJSON = reader.readLine();
				Log.d("json", eventsJSON);
				JSONArray arr = new JSONArray(eventsJSON);
				events = parseEventJSON(arr);
				
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
			
			return events;
		}
		
		
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		//This method will update the UI after the load is finished.
		protected void onPostExecute(ArrayList<Event> events)
		{
			if(loadSuccessful)
			{	
				Log.d("load", "successful");
				
				//Populate list view
				final TimelineAdapter listAdapter = new TimelineAdapter(getActivity(), events);								 
				ListView eventList = (ListView) getView().findViewById(R.id.event_list); //Get the list view
				
				eventList.setAdapter(listAdapter);
				
				//Set click listener which will replace this fragment with the event fragment on click
				OnItemClickListener oc = new OnItemClickListener()
				{

					@Override
					//On item click, we start the individual event activity
					public void onItemClick(AdapterView<?> parent,
							View selectedView, int position, long selectedViewId)
					{
						Event e = listAdapter.getItem(position);
						Intent intent = new Intent(getActivity(), EventActivity.class);
						Bundle args = new Bundle();
						
						//Get formatted times
						String displayFormat = "hh:mm aa";
				        SimpleDateFormat df = new SimpleDateFormat(displayFormat, Locale.US);
				        String stimeText = df.format(e.getStartDate());
						String etimeText = df.format(e.getEndDate());
						
						//Add event information to bundle
						args.putString("e_title", e.getTitle());
						args.putString("e_desc", e.getDescription());
						args.putString("e_stime", stimeText);
						args.putString("e_etime", etimeText);
						args.putString("e_loc", e.getLocation());
						
						//Add bundle to intent
						intent.putExtras(args);
						startActivity(intent);
					}
					
				};
				
				//Add listener to listview
				eventList.setOnItemClickListener(oc);
				//Hide progress wheel
				ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progress_wheel);
				bar.setVisibility(View.GONE);
			}
			else
			{
				Log.d("load", "unsuccessful");
				//Show error textview
				final TextView errorView = (TextView) getView().findViewById(R.id.tline_load_error);
				errorView.setVisibility(View.VISIBLE);
				
				//Hide progress wheel
				final ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progress_wheel);
				bar.setVisibility(View.GONE);
				
				//Hide listview
				ListView eventList = (ListView) getView().findViewById(R.id.event_list);
				eventList.setVisibility(View.GONE);
				
				//Show retry button
				final Button retry = (Button) getView().findViewById(R.id.retry_button);
				//If button is clicked, try the load again
				retry.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						//Hide error textview
						errorView.setVisibility(View.GONE);
						//Hide retry button
						retry.setVisibility(View.GONE);
						
						//Show progress wheel animation
						bar.setVisibility(View.VISIBLE);
						
						//Execute load again
						loader = new EventLoader();
						loader.execute();
					}
					
				});
				retry.setVisibility(View.VISIBLE);	
			}
				
		}
		
		/**
		 * @param obj The JSON object containing the events
		 * @return An arraylist of events
		 * @throws JSONException if parse fails
		 */
		protected ArrayList<Event> parseEventJSON(JSONArray arr) throws JSONException
		{
			ArrayList<Event> events = new ArrayList<Event>();
			for(int i = 0; i < arr.length(); i++)
			{
				String id = arr.getJSONObject(i).getString("id");
				String title = arr.getJSONObject(i).getString("title").trim();
				String location = arr.getJSONObject(i).getString("location").trim();
				String description = arr.getJSONObject(i).getString("description").trim();
				String startDate = arr.getJSONObject(i).getString("startDate").trim();
				String endDate = arr.getJSONObject(i).getString("endDate").trim();
				String lastModified = arr.getJSONObject(i).getString("lastModified").trim();
				
				try
				{
					Event e = new Event(id, title, location, startDate, endDate, lastModified, description);
					events.add(e);
				} catch (ParseException e)
				{
					//Must remove this before release
					Log.d("Event Parsing", "Failed to parse event" + title);
				}
			}
			
			if(events.size() <= 0)
				loadSuccessful = false; //Loading nothing does not qualify as a "successful" load operation
			return events; 
		}
	}

}

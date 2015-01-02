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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

/**
 * A {@link Fragment} subclass which is responsible for displaying the timeline information. It uses
 * an {@link AsyncTask} to load the data and then it updates the UI accordingly.
 * 
 */
public class TimelineFragment extends Fragment
{
	private Context c;
	/**
	 * The list of Events
	 */
	private ArrayList<Event> events;
	/**
	 * Flag stating whether or not the load operation was successful
	 */
	private boolean loadSuccessful;
	/**
	 * Flag stating whether or not the event load from the cache was successful
	 */
	private boolean cacheLoadSuccessful;
	/**
	 * Flag stating whether or not the event list is currently in the refresh process
	 */
	private boolean isRefreshing;
	/**
	 * The loader which performs the async load operation.
	 */
	private EventLoader loader;
	
	
	public TimelineFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		 View v = inflater.inflate(R.layout.fragment_timeline, container, false);
		 
		 if(cacheLoadSuccessful && events.size() > 0)
			 showEventList(v, events);
		 return v;
	}
	
	/**
	 * This method is necessary because an empty, no argument constructor must be provided
	 * for a fragment in Android
	 * @return A new instance of timeline fragment that is executing the load operation
	 */
	@SuppressWarnings("unchecked")
	public static TimelineFragment newInstance(Context c)
	{
		TimelineFragment f = new TimelineFragment();
		f.c = c;
		f.loadSuccessful = false;
		f.isRefreshing = false;
		f.resetLoader();
		
		//Read data from cache
		Object o = CacheManager.readObjectFromCacheFile(c , "events");
		if(o == null)
		{
			f.forceEventListUpdate();
			f.cacheLoadSuccessful = false;
			Log.d("Event Load", "internet");
		}
		else
		{
			f.cacheLoadSuccessful = true;
			f.events = (ArrayList<Event>) o;
			Log.d("Event Load", "cache");
		}
		
		return f;
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
	
	public void forceEventListUpdate()
	{
		resetLoader();
		loader.execute();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	//Needed to override this method to cancel the refresh if this fragment is stopped
	public void onStop()
	{
		super.onStop();
		loader.cancel(true);
		removeHazyForeground(getView());
		((SwipeRefreshLayout) getView().findViewById(R.id.event_list_container)).setRefreshing(false);
	}
	
	/**
	 * Show the event list on the view and hide the progress wheel
	 * @param v The view to modify
	 * @param events The events to show
	 */
	private void showEventList(final View v, ArrayList<Event> events)
	{
		//Populate list view
		final TimelineAdapter listAdapter = new TimelineAdapter(getActivity(), events);								 
		final ListView eventList = (ListView) v.findViewById(R.id.event_list); //Get the list view
		
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
				String displayFormat = "hh:mm aa   MM/dd/yyyy";
		        SimpleDateFormat df = new SimpleDateFormat(displayFormat, Locale.US);
		        String stimeText = df.format(e.getStartDate());
				String etimeText = df.format(e.getEndDate());
				
				//Get rid of leading zeros
				//stimeText=Integer.toString(Integer.parseInt(stimeText.substring(0, 2)));
				//etimeText=Integer.toString(Integer.parseInt(etimeText.substring(0,2)));
				
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
		
		//Show list
		SwipeRefreshLayout listLayout = (SwipeRefreshLayout) v.findViewById(R.id.event_list_container);
		listLayout.setVisibility(View.VISIBLE);
		
		//Hide progress wheel
		ProgressBar bar = (ProgressBar) v.findViewById(R.id.progress_wheel);
		bar.setVisibility(View.GONE);
		
		//Set refresh layout action
		listLayout.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh()
			{
				eventList.setEnabled(false);
				isRefreshing = true;
				forceEventListUpdate();
				showHazyForeground(v);
			}
		});
		
		//Set refresh layout colors
		listLayout.setColorSchemeResources(R.color.dm_orange_primary, R.color.dm_blue_secondary, R.color.GreenYellow);
	}
	
	/**
	 * Show a load error page 
	 * @param v The view to show it on
	 */
	private void showLoadErrorPage(View v)
	{
		//Show error textview
		final TextView errorView = (TextView) v.findViewById(R.id.tline_load_error);
		errorView.setVisibility(View.VISIBLE);
		
		//Hide progress wheel
		final ProgressBar bar = (ProgressBar) v.findViewById(R.id.progress_wheel);
		bar.setVisibility(View.GONE);
		
		//Hide listview
		v.findViewById(R.id.event_list_container).setVisibility(View.GONE);
		
		//Show retry button
		final Button retry = (Button) v.findViewById(R.id.retry_button);
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
	
	/**
	 * Show a toast if the refresh operation fails
	 */
	private void showRefreshErrorToast()
	{
		Toast toast = Toast.makeText(c, "Could not refresh data", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 40);
		toast.show();
	}
	
	/**
	 * Shows a hazy view over all the other views
	 * @param v The container view
	 */
	private void showHazyForeground(View v)
	{
		View hazyView = v.findViewById(R.id.hazy_foreground);
		hazyView.setVisibility(View.VISIBLE);
		hazyView.bringToFront();
	}
	private void removeHazyForeground(View v)
	{
		View hazyView = v.findViewById(R.id.hazy_foreground);
		hazyView.setVisibility(View.GONE);
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
				//Log.d("json", eventsJSON);
				JSONArray arr = new JSONArray(eventsJSON);
				events = parseEventJSON(arr);
				
				//Write data to cache
				CacheManager.writeObjectToCacheFile(getActivity(), events, "events");
				
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
			final SwipeRefreshLayout l = (SwipeRefreshLayout) getView().findViewById(R.id.event_list_container);
			final ListView eventList = (ListView) getView().findViewById(R.id.event_list); //Get the list view
			if(loadSuccessful)
			{	
				Log.d("load", "successful");
				
				showEventList(getView(), events);
				
				//Change the flag once we are done with the load operation
				if(isRefreshing)
				{
					isRefreshing = false;
					l.setRefreshing(false);
					removeHazyForeground(getView());
					eventList.setEnabled(true); //Enable eventlist
				}
			}
			else
			{
				Log.d("load", "unsuccessful");
				if(isRefreshing)
				{
					showRefreshErrorToast();
					isRefreshing = false;
					l.setRefreshing(false);
					removeHazyForeground(getView());
					eventList.setEnabled(true);// Enable eventlist
				}
				else
					showLoadErrorPage(getView());
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

}

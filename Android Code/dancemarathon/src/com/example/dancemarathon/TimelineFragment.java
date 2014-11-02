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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class TimelineFragment extends Fragment
{
	/**
	 * The list of Events
	 */
	@SuppressWarnings("unused")
	private ArrayList<Event> events;
	/**
	 * Flag stating whether or not the load operation was successful
	 */
	private boolean loadSuccessful;
	private EventLoader loader;

	public TimelineFragment()
	{
		// Required empty public constructor
		loadSuccessful = false;
		loader = new EventLoader();
		loader.execute(); //Perform the load operation
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_timeline, container, false);
	}
	
	public static TimelineFragment newInstance()
	{
		TimelineFragment f = new TimelineFragment();
		return f;
	}
	
	//Needed to override this method to cancel the async task if this fragment is destroyed
	public void onDestroyView()
	{
		super.onDestroyView();
		loader.cancel(true);
	}
	
	/**
	 * This class is responsible for loading the events. It is necessary because Android
	 * does not allow you to have loading operations on the same thread as the UI.
	 */
	private class EventLoader extends AsyncTask<Void, Double, ArrayList<Event>>
	{
		
		@Override
		protected ArrayList<Event> doInBackground(Void... params)
		{
			ArrayList<Event> events = new ArrayList<Event>();
			try
			{	
				URL url = new URL("http://mickmaccallum.com/ian/events.php"); //The path to the webservice 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				//Parse JSON response
				String eventsJSON = reader.readLine();
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
		
		
		//This class will update the UI after the load is finished.
		protected void onPostExecute(ArrayList<Event> events)
		{
			if(loadSuccessful)
			{	
				Log.d("load", "successful");
				
				//Populate list view
				ArrayAdapter<Event> listAdapter = new ArrayAdapter<Event>(getActivity(), android.R.layout.simple_list_item_1);
				populateAdapter(listAdapter, events); 									 //Add the events to the list adapter
				ListView eventList = (ListView) getView().findViewById(R.id.event_list); //Get the list view
				
				eventList.setAdapter(listAdapter);
			
				//Hide progress wheel
				ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progress_wheel);
				bar.setVisibility(View.GONE);
			}
			else
				Log.d("load", "unsuccessful");
		}
		
		private void populateAdapter(ArrayAdapter<Event> l, ArrayList<Event> events)
		{
			for(int i = 0; i < events.size(); i++)
			{
				Event e = events.get(i);
				l.add(e);
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

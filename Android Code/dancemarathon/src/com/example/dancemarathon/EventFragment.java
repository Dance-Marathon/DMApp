package com.example.dancemarathon;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class EventFragment extends Fragment
{

	protected Event e;
	
	public EventFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_event, container, false);
		
		TextView title = (TextView) v.findViewById(R.id.event_page_title);
		TextView description = (TextView) v.findViewById(R.id.event_page_desc);
		TextView stime = (TextView) v.findViewById(R.id.event_page_stime);
		TextView etime = (TextView) v.findViewById(R.id.event_page_etime);
		TextView location = (TextView) v.findViewById(R.id.event_page_loc);
		
		Bundle args = this.getArguments();
		//Set event title
		title.setText(args.getString("event_title"));
		//Set event description
		description.setText(args.getString("event_desc"));
		
		//Set event start and end times
		stime.setText(args.getString("event_stime"));
		etime.setText(args.getString("event_etime"));
		
		//Set location
		location.setText(args.getString("event_location"));
		
		return v;
	}
	
	public static EventFragment newInstance(Event e)
	{
		EventFragment ef = new EventFragment();
		ef.e = e;
		
		//Format event start and end times
		SimpleDateFormat df = new SimpleDateFormat("hh:mm aa", Locale.US);
		String start = df.format(e.getStartDate());
		String end = df.format(e.getEndDate());
		
		//Put event fields on the bundle so we can still get the information 
		//even if the application tries to recreate the fragment with the empty constructor
		Bundle b = new Bundle();
		b.putString("event_title", e.getTitle());
		b.putString("event_desc", e.getDescription());
		b.putString("event_stime", start);
		b.putString("event_etime", end);
		b.putString("event_location", e.getLocation());

		ef.setArguments(b);
		
		return ef;
	}

}

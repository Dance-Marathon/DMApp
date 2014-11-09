package com.example.dancemarathon;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimelineAdapter extends ArrayAdapter<Event>
{

	private Context context;
	/**
	 * The events to display
	 */
	private ArrayList<Event> events;
	
	public TimelineAdapter(Context c, ArrayList<Event> events)
	{
		//Must make call to the parent constructor
		super(c, R.layout.timeline_item_view, events);
		
		this.context = c;
		this.events = events;
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return events.size();
	}

	@Override
	public Event getItem(int position)
	{
		// TODO Auto-generated method stub
		return events.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View itemView;
		//Create inflater 
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //If convert view is null, we need to make the individual view for the event
        if(convertView == null)
        {
	        //Get the individual item view
	        itemView = inflater.inflate(R.layout.timeline_item_view, parent, false);
	        
	        //Get title and location text views
	        TextView title = (TextView) itemView.findViewById(R.id.tlineitem_title);
	        TextView location = (TextView) itemView.findViewById(R.id.tlineitem_location);
	        
	        //Set text views
	        title.setText(events.get(position).getTitle());
	        location.setText("Location: " + events.get(position).getLocation());
        }
        //Else we can use the recycled view passed in as convertView
        else
        	itemView = convertView;

        return itemView;
	}

}

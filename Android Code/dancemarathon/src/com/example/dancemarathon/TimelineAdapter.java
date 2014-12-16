package com.example.dancemarathon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
	        
	        //Set all the values for the view
	        setItemView(itemView, position);
	        
        }
        //Else we can use the recycled view passed in as convertView
        else
        	itemView = convertView;

        return itemView;
	}
	
	/**
	 * This method sets the values of all the views which comprise the individual event view.
	 * @param v The view to modify
	 * @param position The position of the item
	 */
	private void setItemView(View itemView, int position)
	{
		Event e = events.get(position);
		
		//Get title and location text views
        TextView title = (TextView) itemView.findViewById(R.id.tlineitem_title);
        TextView location = (TextView) itemView.findViewById(R.id.tlineitem_location);
        TextView time = (TextView) itemView.findViewById(R.id.tlineitem_startTime);
        TextView month = (TextView) itemView.findViewById(R.id.tlineitem_month);
        TextView day = (TextView) itemView.findViewById(R.id.tlineitem_day);
        
        //Set text views //
        
        //Set title
        String titleText = e.getTitle();
        if(titleText.length() > 22)
        	titleText = titleText.substring(0, 21);
        title.setText(titleText);
        
        //Set location
        location.setText("Location: " + e.getLocation());
        
        //Set time
        String displayFormat = "hh:mm aa";
        SimpleDateFormat df = new SimpleDateFormat(displayFormat, Locale.US);
        String timeText = df.format(e.getStartDate());
        time.setText(timeText);
        
        //Set month
        String monthText = e.getMonthText(true);
        month.setText(monthText);
        
        //Set day
        String dayText = Integer.toString(e.getDay(true));
        day.setText(dayText);
        
	}

}

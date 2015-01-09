package com.example.dancemarathon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

/**
 * @author Chris
 * This class is responsible for sending notifications to the user
 * when DM events are close at hand.
 */
public class NotificationService extends IntentService {
	
	/**
	 * The lesser proximity to judge events by
	 */
	private static int eventProx1 = 5;
	
	/**
	 * The greater proximity to judge events by
	 */
	private static int eventProx2 = 15;
	
	public NotificationService() {
		super("NotificationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		//Set up receiver to recieve TIME_TICK intents
		BroadcastReceiver receiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				//The time ticks every minute
				if(intent.getAction().equals(Intent.ACTION_TIME_TICK))
				{
					setupEventNotifications();
				}
			}
			
		};
		
		this.registerReceiver(receiver, new IntentFilter());
	}
	
	/**
	 * This method does all the necessary gruntwork to setup the event notifications
	 * for all events currently in the cache file.
	 */
	private void setupEventNotifications()
	{
		Object o = CacheManager.readObjectFromCacheFile(this, "events");
		//Stop this service if there is no event cache file
		
		if(o != null)
			this.stopSelf();
		else
		{
			if(verifyEventArrayList((ArrayList<?>) o))
			{
				ArrayList<Event> allEvents = (ArrayList<Event>) o;
				SparseArray<ArrayList<Event>> upcomingEvents = new SparseArray<ArrayList<Event>>();
				
				//Add events that are 5 and 15 mins away to the hashmap
				upcomingEvents.put(eventProx1, checkForUpcomingEvents(allEvents,eventProx1));
				upcomingEvents.put(eventProx2, checkForUpcomingEvents(allEvents,eventProx2));
				
				//Create the notifications
				createEventNotifications(upcomingEvents.get(eventProx1), eventProx1);
				createEventNotifications(upcomingEvents.get(eventProx2), eventProx2);
			}
			
		}
	}
	
	/**
	 * This method creates individual notifications for each event
	 * in the input arraylist.
	 * @param events The events
	 * @param proximity Minutes until the events start
	 */
	private void createEventNotifications(ArrayList<Event> events, int proximity)
	{
		Iterator<Event> i = events.iterator();
		while(i.hasNext())
		{
			Event e = i.next();
			createEventNotification(e, proximity);
		}
	}
	
	/**
	 * This method creates an individual notification for the specified event
	 * @param e The event
	 * @param proximity Minutes until the event starts
	 */
	private void createEventNotification(Event e, int proximity)
	{
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.launcher_icon)
		        .setContentTitle("Event: " + e.getTitle())
		        .setContentText("Happening in " + proximity + " minutes!")
		        .setAutoCancel(true);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(1, mBuilder.build());
	}
	
	
	/**
	 * This method checks for events which are within the designated time proximity
	 * @param events The list of events
	 * @param timeProximity The proximity an event must be to be considered upcoming
	 * @return The list of upcoming events
	 */
	private ArrayList<Event> checkForUpcomingEvents(ArrayList<Event> events, int timeProximity)
	{
		ArrayList<Event> upcoming = new ArrayList<Event>();
		
		Iterator<Event> i = events.iterator();
		while(i.hasNext())
		{
			Event e = i.next();
			int startDateInMins = (int) (e.getStartDate().getTime() / Calendar.MINUTE);
			int currentTimeInMins = (int) (Calendar.getInstance().getTime().getTime() / Calendar.MINUTE);
			
			if((startDateInMins + timeProximity) == currentTimeInMins)
			{
				upcoming.add(e);
			}
		}
		
		return upcoming;
		
	}
	
	
	/**
	 * This method just produces a basic test notification
	 */
	private void testNotification()
	{
		NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.launcher_icon)
	        .setContentTitle("My notification")
	        .setContentText("Hello World!")
	        .setAutoCancel(true);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(1, mBuilder.build());
	}
	
	/**
	 * This method verifies that every object in the list is of type {@link Event}
	 * @param list
	 * @return True if every object is Event. False otherwise
	 */
	private boolean verifyEventArrayList(ArrayList<?> list)
	{
		Iterator<?> i = list.iterator();
		while(i.hasNext())
		{
			Object o = i.next();
			if(!(o instanceof Event))
				return false;
		}
		
		return true;
	}
	
	


}

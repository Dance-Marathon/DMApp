package com.example.dancemarathon;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author Chris Whitten
 * This service is responsible for sending notifications to the user
 * when DM events are close at hand.
 */
public class NotificationService extends Service {
	
	/**
	 * The lesser proximity to judge events by
	 */
	private static int eventProx1 = 5;
	/**
	 * The greater proximity to judge events by
	 */
	private static int eventProx2 = 15;
	
	/**
	 * Keeps track of the number of notifications this service has published
	 */
	private int numActiveNotifications;
	
	//Set up receiver to receive TIME_TICK intents
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//testNotification("In on receiver");
			//The time ticks every minute
			//Log.d("Notifications", "In on receive");
			if(intent.getAction().equals(Intent.ACTION_TIME_TICK))
			{
				setupEventNotifications();
				
				//Log.d("Notifications", "Done with event notification setup");
			}
			
			//Recreate the service and delete the old one
			NotificationService.this.stopSelf();
			context.startService(new Intent(context, NotificationService.class));
			
		}
		
	};;
	
	public NotificationService() {
		super();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		// TODO Auto-generated method stub
		numActiveNotifications = 0;
		//Log.d("Notification", "Registering receiver");
		this.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		return Service.START_STICKY;
	}
	
	public void onDestroy()
	{
		this.unregisterReceiver(receiver);
	}
	
	/**
	 * This method does all the necessary gruntwork to setup the event notifications
	 * for all events currently in the cache file.
	 */
	private void setupEventNotifications()
	{
		Object o = CacheManager.readObjectFromCacheFile(this, "events");
		//Stop this service if there is no event cache file
		if(o == null)
			this.stopSelf();
		else
		{
			Log.d("debug", "In else");
			//if(verifyEventArrayList((ArrayList<?>) o))
			{
				//Log.d("debug", "In if");
				ArrayList<Event> allEvents = (ArrayList<Event>) o;
				SparseArray<ArrayList<Event>> upcomingEvents = new SparseArray<ArrayList<Event>>();
				
				//Test Events
				try {
					Event t1 = new Event("1","Test Event", "blah", "2015-01-13 19:54:00", "2015-01-13 19:54:00", "2015-01-13 06:00:00", "blah");
					allEvents.add(t1);
					//createEventNotification(t1, 5, 1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
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
			numActiveNotifications++;
			Log.d("debug", "Creating " + e.getTitle());
			createEventNotification(e, proximity, numActiveNotifications);
		}
	}
	
	/**
	 * This method creates an individual notification for the specified event
	 * @param e The event
	 * @param proximity Minutes until the event starts
	 */
	private void createEventNotification(Event e, int proximity, int mId)
	{
		//Set the pending intent for when the user clicks the notification
		PendingIntent pIntent = getMainPendingIntent();
				
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.launcher_icon)
		        .setContentTitle("Event: " + e.getTitle())
		        .setContentText("Happening in " + proximity + " minutes!")
		        .setAutoCancel(true)
		        .setContentIntent(pIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(mId, mBuilder.build());
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
			
			//Get current time
			Calendar c =  Calendar.getInstance();
			int currentTimeInMins = c.get(Calendar.MINUTE);
			
			//Get event time
			c.setTime(e.getStartDate());
			int startDateInMins = c.get(Calendar.MINUTE);
			
			int timeUntil = startDateInMins - currentTimeInMins;
			if(startDateInMins > currentTimeInMins && timeUntil == timeProximity)
			{
				upcoming.add(e);
				Log.d("Upcoming", e.getTitle());
			}
		}
		
		Log.d("Upcoming", String.valueOf(upcoming.size()));
		return upcoming;
		
	}
	
	/**
	 * This method creates a pending intent to open the SwipeActivity when
	 * the notification is pressed.
	 * @return The pending intent to use
	 */
	private PendingIntent getMainPendingIntent()
	{
		Intent intent = new Intent(this, SwipeActivity.class);
		intent.putExtra("start_source", "Service");
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(SwipeActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return pIntent;
	}
	
	/**
	* This method just produces a basic test notification
	 */
	private void testNotification(String title)
	{
		//Set the pending intent for when the user clicks the notification
		PendingIntent pIntent = getMainPendingIntent();
		
		//Create notification
		NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.launcher_icon)
	        .setContentTitle(title)
	        .setContentText("Hello World!")
	        .setAutoCancel(true)
	        .setContentIntent(pIntent);
		
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

	
	@Override
	//This method is a required override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	
	


}

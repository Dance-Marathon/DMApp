package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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

public class AnnouncementService extends Service {
	
	private static int minuteCounter = 0;
	private ArrayList<Announcement> ments = new ArrayList<Announcement>();
	
	//Set up receiver to receive TIME_TICK intents
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			//The time ticks every minute
			if(intent.getAction().equals(Intent.ACTION_TIME_TICK))
			{
				minuteCounter++;
				if(minuteCounter == 1)
				{
					minuteCounter = 0;
					if(getNewAnnouncements())
					{
						notifyUser(18); //Notify user new announcements are ready. 18 is random id.
					}
				}
			}
		}
		
	};;
		
	public AnnouncementService() {
		super();
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		this.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		ments = readCache();
		return Service.START_STICKY;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Announcement> readCache()
	{
		Object o = CacheManager.readObjectFromCacheFile(this, "announcements");
		if(o != null)
			return (ArrayList<Announcement>) o;
		else
			return new ArrayList<Announcement>();
	}
	
	private boolean getNewAnnouncements()
	{
		ArrayList<Announcement> announcements = new ArrayList<Announcement>();
		
		String path;
		try {
			path = new ConfigFileReader(this).getSetting("announcementsPath");
			URL url = new URL(path); //The path to the webservice 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			//Parse JSON response
			String announcementsJSON = reader.readLine();
			// Log.d("json", eventsJSON);
			JSONArray arr = new JSONArray(announcementsJSON);
			announcements = parseAnnouncementsJSON(arr);
			
			//If new announcements have been found, update ments and cache
			if(announcements.size() > ments.size())
			{
				ments = announcements;
				CacheManager.writeObjectToCacheFile(this, ments, "announcements");
				return true;
			}	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return false;
			
		
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
		
		return announcements; 
	}
	
	private void notifyUser(int mId)
	{
		//Set vibration pattern
		long[] pattern = {1, 1000};
		
		//Set the pending intent for when the user clicks the notification
		PendingIntent pIntent = getMainPendingIntent();
				
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.launcher_icon)
		        .setContentTitle("New Announcements Available!")
		        .setContentText("Click to see new announcements")
		        .setAutoCancel(true)
		        .setVibrate(pattern)
		        .setContentIntent(pIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(mId, mBuilder.build());
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
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

package com.uf.dancemarathon;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * This class provides functionality for google analytics
 * @author Chris
 *
 */
public class TrackerManager
{
	/**
	 * Send a screen view hit using the default app tracker
	 * @param a The application object
	 * @param screenName The name of the screen
	 */
	public static void sendScreenView(MyApplication a, String screenName)
	{
		Tracker t = a.getTracker(MyApplication.TrackerName.APP_TRACKER);
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * Send a screen view hit using the input tracker
	 * @param screenName The screen name
	 * @param t The tracker to use
	 */
	public static void sendScreenView(String screenName, Tracker t)
	{
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * Send an event hit using the default tracker
	 * @param a The application object
	 * @param category The category of the event
	 * @param action The action that occurred
	 * @param label The label of the event
	 */
	public static void sendEvent(MyApplication a, String category, String action, String label)
	{
		Tracker t = a.getTracker(MyApplication.TrackerName.APP_TRACKER);
		t.send(new HitBuilders.EventBuilder()
		.setCategory(category)
		.setAction(action)
		.setLabel(label)
		.build());
	}
}

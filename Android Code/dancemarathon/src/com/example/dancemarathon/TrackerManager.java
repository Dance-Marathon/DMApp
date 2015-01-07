package com.example.dancemarathon;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackerManager
{
	public static void sendScreenView(MyApplication a, String screenName)
	{
		Tracker t = a.getTracker(MyApplication.TrackerName.APP_TRACKER);
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	public static void sendScreenView(MyApplication a, String screenName, Tracker t)
	{
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
}

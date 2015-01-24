package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class SocialMedia extends ActionBarActivity {

	/**
	 * This is the DM over the years video
	 */
	private static String defaultYoutubeVideoId = "ZRc6rjZleMs";
	private static String twitter_Id = "twitter://user?user_id=34755385";
	private static String facebook_Id = "fb://page/116374146706";
	private static String instagram_Id = "http://instagram.com/dmatuf";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_media);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Social Media");
		
		int color = getResources().getColor(R.color.dm_orange_primary);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	// onClick method to open links
	public void openLink(View view)
	{
		// Get media type from tag
	    String media = (String)view.getTag();
	
	    Intent intent = new Intent();
	    
	    // Associate the respective intent with the social media
	    if (media.equals("Facebook"))
	    {
	    	intent = getOpenFacebookIntent(this);	
	    }
	    else if (media.equals("Twitter"))
	    {
	    	intent = getOpenTwitterIntent(this);
	    }
	    else if (media.equals("Instagram"))
	    {
	    	intent = getOpenInstagramIntent(this);
	    }
	    else if (media.equals("YouTube"))
	    {
	    	intent = getOpenYouTubeIntent(this);
	    }
	    else
	    {
	    	intent = new Intent(Intent.ACTION_VIEW, Uri.parse(media));
	    }
	    startActivity(intent);
	}
	
	// Intent to open Facebook in either its respective app or the browser
	public static Intent getOpenFacebookIntent(Context context)
	{
		// Open Facebook page in Facebook app
		try
		{
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMedia.facebook_Id));
		}
		// Open Facebook page in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/floridaDM"));
		}
	}
	
	// Intent to open Twitter in either its respective app or the browser
	public static Intent getOpenTwitterIntent(Context context)
	{
		// Open Twitter profile in Twitter app
		try
		{
		    context.getPackageManager().getPackageInfo("com.twitter.android", 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMedia.twitter_Id));
		}
		// Open Twitter profile in browser
		catch (Exception e)
		{
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/floridadm"));
		}
	}
	
	// Intent to open Instagram in either its respective app or the browser
	public static Intent getOpenInstagramIntent(Context context)
	{
		// Open Instagram profile in Instagram app
		try{
			context.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
			return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMedia.instagram_Id));
		}
		// Open Instagram profile in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dmatuf"));
		}
	}
	
	// Intent to open YouTube in either its respective app or the browser
	public static Intent getOpenYouTubeIntent(Context context)
	{
		
		// Open YouTube channel in YouTube app
		try
		{
			context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + SocialMedia.defaultYoutubeVideoId));
		}
		// Open YouTube channel in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/UFDanceMarathon"));
		}
	}
}

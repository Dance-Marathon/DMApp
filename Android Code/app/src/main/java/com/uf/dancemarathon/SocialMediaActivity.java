package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class SocialMediaActivity extends AppCompatActivity {

    //Package names for social media apps
    private static String TWITTER_PACKAGE = "com.twitter.android";
    private static String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static String YOUTUBE_PACKAGE = "com.google.android.youtube";

	//IDs for Dance Marathon page on various social media sites to open through their respective apps
	private static String TWITTER_ID = "twitter://user?user_id=34755385";
	private static String FACEBOOK_ID = "fb://page/116374146706";
	private static String INSTAGRAM_ID = "http://instagram.com/dmatuf";
    private static String YOUTUBE_ID = "ZRc6rjZleMs"; //DM over the years video

    //Websites for Dance Marathon page on various social media sites to open through web browser
    private static String TWITTER_URL = "https://www.twitter.com/floridadm";
    private static String FACEBOOK_URL = "https://www.facebook.com/floridaDM";
    private static String INSTAGRAM_URL = "https://www.instagram.com/dmatuf";
    private static String YOUTUBE_URL = "https://www.youtube.com/user/UFDanceMarathon";

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

		int color = getResources().getColor(R.color.action_bar_color);
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
			context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMediaActivity.FACEBOOK_ID));
		}
		// Open Facebook page in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
		}
	}
	
	// Intent to open Twitter in either its respective app or the browser
	public static Intent getOpenTwitterIntent(Context context)
	{
		// Open Twitter profile in Twitter app
		try
		{
		    context.getPackageManager().getPackageInfo(TWITTER_PACKAGE, 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMediaActivity.TWITTER_ID));
		}
		// Open Twitter profile in browser
		catch (Exception e)
		{
		    return new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL));
		}
	}
	
	// Intent to open Instagram in either its respective app or the browser
	public static Intent getOpenInstagramIntent(Context context)
	{
		// Open Instagram profile in Instagram app
		try{
			context.getPackageManager().getLaunchIntentForPackage(INSTAGRAM_PACKAGE);
			return new Intent(Intent.ACTION_VIEW, Uri.parse(SocialMediaActivity.INSTAGRAM_ID));
		}
		// Open Instagram profile in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_URL));
		}
	}
	
	// Intent to open YouTube in either its respective app or the browser
	public static Intent getOpenYouTubeIntent(Context context)
	{
		
		// Open YouTube channel in YouTube app
		try
		{
			context.getPackageManager().getPackageInfo(YOUTUBE_PACKAGE, 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + SocialMediaActivity.YOUTUBE_ID));
		}
		// Open YouTube channel in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL));
		}
	}
}

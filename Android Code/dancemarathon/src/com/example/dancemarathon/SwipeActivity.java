package com.example.dancemarathon;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SwipeActivity extends ActionBarActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a 
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	KinteraUser user;
	private String[] mOtherOptions;
    static final int GET_USER_REQUEST = 1;
	
	//These methods allow us to maintain the state of the user//
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable("user", user);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		//Get user from savedInstanceState
		user = savedInstanceState.getParcelable("user");
	}
	//
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		//Hide Action Bar
		getSupportActionBar().hide();
		
		setUpPagers();
		setUpNavDrawer();
		user = (KinteraUser) CacheManager.readObjectFromCacheFile(this, "user");
		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int pos)
			{
				if(pos == 0)
					mDrawerLayout.openDrawer(Gravity.START);
			}
			
		});
	}
	
	private void setUpPagers()
	{
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		// Set the ViewPager to Home
		mViewPager.setCurrentItem(1, false);

		// Change PagerTabStrip spacing
		PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		tabStrip.setTextSpacing(0);
		tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PT, 7);
	}
	
	private void setUpNavDrawer()
	{
		 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerList = (ListView) findViewById(R.id.left_drawer);
	     mOtherOptions = getResources().getStringArray(R.array.navList);
	     
	     // Set the adapter for the list view
	     mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	           R.layout.nav_drawer_item, R.id.nav_item, mOtherOptions));
	     // Set the list's click listener
	     mDrawerList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				switch(position)
				{
				case 2:openFundraisingActivity();
				}
				
			}
	    	 
	     });
	     
	     mDrawerLayout.setDrawerListener(new DrawerListener(){

			@Override
			public void onDrawerClosed(View arg0)
			{
			}

			@Override
			public void onDrawerOpened(View arg0)
			{
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1, false);
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDrawerStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
				
			}
	    	 
	     });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method handles opening of the my fundraising progress activity.
	 * If a user has been defined so far, then we open the user activity.
	 * Else, the login activity is opened
	 */
	private void openFundraisingActivity()
	{
		if(user instanceof KinteraUser)
		{
			Intent intent = new Intent(this, UserActivity.class);
			Bundle b = new Bundle();
			b.putParcelable("user", user);
			intent.putExtras(b);
			startActivityForResult(intent, GET_USER_REQUEST);
		}
		else
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, GET_USER_REQUEST);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == GET_USER_REQUEST)
		{
			if(resultCode == RESULT_OK)
			{
				user = data.getExtras().getParcelable("user");
			}
			else if(resultCode == RESULT_CANCELED)
			{
				user = null;
			}
		}
	}
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return the fragment that corresponds to the position
			switch (position)
			{
			case 0:return new Fragment(); //Return blank fragment because this will be covered by nav drawer
			case 1:return HomeFragment.newInstance();
			case 2:return TimelineFragment.newInstance(SwipeActivity.this);
			case 3:return MtkFragment.newInstance();
			}
			return null;
		}

		@Override
		public int getCount()
		{
			// Get total number of pages
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			Drawable drawerIcon = getResources().getDrawable(R.drawable.ic_drawer);
			SpannableStringBuilder sb = new SpannableStringBuilder("  "); 

		    drawerIcon.setBounds(0, 0, drawerIcon.getIntrinsicWidth(), drawerIcon.getIntrinsicHeight()); 
		    ImageSpan span = new ImageSpan(drawerIcon, ImageSpan.ALIGN_BASELINE); 
		    sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		   
		    
			Locale l = Locale.getDefault();
			switch (position)
			{
			case 0: return sb;
			case 1:return getString(R.string.title_section1).toUpperCase(l);
			case 2:return getString(R.string.title_section2).toUpperCase(l);
			case 3:return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
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
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/116374146706"));
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
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=34755385"));
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
			return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/dmatuf"));
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
			return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"));
		}
		// Open YouTube channel in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/UFDanceMarathon"));
		}
	}
}


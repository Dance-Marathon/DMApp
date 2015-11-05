package com.uf.dancemarathon;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.reflect.Field;


public class HomeActivity extends AppCompatActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
	private KinteraUser user;
	private String[] mDrawerNames;
	boolean trackEnabled = false;
	private String ACTION_BAR_TITLE = "Welcome to Dance Marathon!";

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
		setContentView(R.layout.activity_home);

        setupActionBar();
		setupNavDrawer();

        //Add HomeFragement
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.home_fragment_container, HomeFragment.newInstance(this)).commit();
	}

    private void setupActionBar(){
        //Set action bar title and color
        ActionBar bar = getSupportActionBar();
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + ACTION_BAR_TITLE + "</font>"));
        int color = getResources().getColor(R.color.action_bar_color);
        ColorDrawable cd = new ColorDrawable();
        cd.setColor(color);
        bar.setBackgroundDrawable(cd);

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }
	
	
	protected void onStart()
	{
		super.onStart();

        //Maintain user state
        user = (KinteraUser) CacheManager.readObjectFromCacheFile(this, CacheManager.USER_FILENAME);

		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "HomeActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.HOME_ACTIVITY_NAME);
			trackEnabled = true;
		}
		
		//Don't show notifications if user is in-app
		stopService(new Intent(this, NotificationService.class));
		stopService(new Intent(this, AnnouncementService.class));
		
		//If this activity was started from the service, go to timeline
		if(this.getIntent().hasExtra("start_source"))
		{
			if(this.getIntent().getStringExtra("start_source").equals("Service"))
				openActivity(CalendarActivity.class);
		}
	}
	
	protected void onStop()
	{
		super.onStop();
		
		//Could not use onDestroy because it is not always called
		
		//Start event notification service
		startService(new Intent(this, NotificationService.class));
		//Start the announcements service
		startService(new Intent(this, AnnouncementService.class));
	}

	
	/**
	 * This method handles the initializations for the navigation drawer
	 */
	private void setupNavDrawer()
	{
		 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerList = (ListView) findViewById(R.id.left_drawer);
	     mDrawerNames = getResources().getStringArray(R.array.navList);

        //Increase the width of the window for swipe gesture that will open the drawer
        increaseNavDrawerEdgeSize(3);

         //Set the action bar icon behavior
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set the adapter for the list view
	     mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                 R.layout.nav_drawer_item, R.id.nav_item, mDrawerNames));
	     // Set the list's click listener
	     mDrawerList.setOnItemClickListener(new OnItemClickListener() {

             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                                     int position, long id) {
                 switch (position) {
                     case 0:openActivity(MapActivity.class);break;
                     case 1:openActivity(FAQActivity.class);break;
                     case 2:openActivity(CalendarActivity.class);break;
                     case 3:openActivity(SocialMediaActivity.class);break;
                     case 4:openActivity(MtkActivity.class);break;
                     case 5:openFundraisingActivity();break;
                     case 6:openActivity(AboutActivity.class);break;
                     case 7:openActivity(ContactUsActivity.class);break;
                 }

             }

         });
	}

    /**
     * This method increases the width of the window which a user can use to drag open the nav drawer.
     * @param factor currWidth * factor = new width
     * @return true if the increase was successful
     */
    private boolean increaseNavDrawerEdgeSize(int factor)
    {
        Field mDragger = null;
        try {
            mDragger = mDrawerLayout.getClass().getDeclaredField("mLeftDragger");

            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(mDrawerLayout);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, edge * factor);
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IllegalAccessException e) {
           return false;
        }

        return true;
    }

    private void openActivity(Class activityClass)
    {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    /**
     * This method handles opening of the my fundraising progress activity.
     * If a user has been defined so far, then we open the user activity.
     * Else, the //Login activity is opened
     */
    private void openFundraisingActivity()
    {
        if(user instanceof KinteraUser)
        {
            Intent intent = new Intent(this, UserActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("user", user);
            intent.putExtras(b);
            startActivity(intent);
        }
        else
        {
            openActivity(LoginActivity.class);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(mDrawerToggle.onOptionsItemSelected(item))
		    return true;

        return super.onOptionsItemSelected(item);
	}
}


package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkProfile extends ActionBarActivity
{
	Kid kid;
	
	public MtkProfile()
	{
		// Required empty public constructor
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtk_profile);
		
		//Get user from intent
		Kid kid = getIntent().getExtras().getParcelable("kid");
		this.kid = kid;
		
		setFields(kid);
		
		final TextView mile = (TextView) findViewById(R.id.milestone);
		mile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				openLink((String)mile.getTag());
				
			}
			
		});
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle(kid.getName());
		
		int color = getResources().getColor(R.color.dm_orange_primary);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
		
		//Set milestone onclick
		
		
	}
	
	public static MtkProfile newInstance()
	{
		MtkProfile f = new MtkProfile();
		return f;
	}
	
	private void setFields(final Kid kid)
	{
		ImageView pic  = (ImageView) findViewById(R.id.kid_pic);
		TextView story = (TextView) findViewById(R.id.kid_story);
		TextView mile = (TextView) findViewById(R.id.milestone);
		
		pic.setImageResource(kid.getImageId(this));
		story.setText(kid.getStory());
		
		if (kid.getYoutube_id().length() > 5)
		{
			mile.setTag(kid.getYoutube_id());
			mile.setText("View " + kid.getName() + "'s Milestone");
		}
		
		FontSetter.setFont(this, FontSetter.fontName.AGBReg, story);
	}
	
	public void openLink(String youtubeId)
	{
	    //Log.d("tag", youtubeId);
	    Intent intent = getOpenYouTubeIntent(this, youtubeId);
	    startActivity(intent);
	}
	
	public static Intent getOpenYouTubeIntent(Context context, String id)
	{
		
		// Open YouTube video in YouTube app
		try
		{
			context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
		}
		// Open YouTube video in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
		}
	}
}

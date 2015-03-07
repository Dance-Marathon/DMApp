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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkProfile extends ActionBarActivity
{
	Kids kid;
	
	// Array of kids who have Milestones on YouTube
	private String [] milestone_name = {
			"Ayden M.", "Anna Rose", 
			"Nathan F.", "Geoffrey P.", 
			"Kasey V.", "Alyssa Mu.", 
			"Hyla M.", "Ava M.", 
			"Tyler P.", "Tyler S.",
			"Catriona C.", "Jessica",
			"Alyssa Ma.", "Zander W.",
			"Michael S.", "Miranda L.",
			"Nick M."};
	
	// Milestone YouTube IDs
	private String[] milestone = {
		"Iz_TYdJE-fM", // Ayden
		"HGpu_SIposk", // Anna Rose
		"W2IOh6_uqD0", // Nathan F
		"Ae5pgULMCyw", // Geoffrey
		"pQ15zlnb2ts", // Kasey
		"X4aY3Zd_Iuw", // Alyssa Mu
		"YFdlEa-t7kw", // Hyla
		"H937Bdls_VE", // Ava
		"46J9JKLYRT0", // Tyler P.
		"DjkiKfeeIRY", // Tyler S.
		"QgdWh9dOcLI", // Catriona 
		"-0BwBQk4ZW4", // Jessica
		"zaeB-IZTCg8", // Alyssa Ma
		"tN_nf45CRUQ", // Zander
		"VBiZWCsOQuA", // Michael S
		"GZReuPztjUg", // Miranda L.
		"Lahp4X1t6kI" // Nick M.
	};
	
	public MtkProfile()
	{
		// Required empty public constructor
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtk_profile);
		
		//Get user from intent
		Kids kid = getIntent().getExtras().getParcelable("kid");
		this.kid = kid;
		
		setFields(kid);
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle(kid.getName());
		
		int color = getResources().getColor(R.color.dm_orange_primary);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
		
		
	}
	
	public static MtkProfile newInstance()
	{
		MtkProfile f = new MtkProfile();
		return f;
	}
	
	private void setFields(final Kids kid)
	{
		ImageView pic  = (ImageView) findViewById(R.id.kid_pic);
		TextView story = (TextView) findViewById(R.id.kid_story);
		TextView mile = (TextView) findViewById(R.id.milestone);
		
		pic.setImageResource(kid.getImageId(this));
		story.setText(kid.getStory());
		
		for (int index = 0; index < milestone.length; index++)
		{
			if (kid.getName().equals(milestone_name[index]))
			{
				mile.setText("View " + kid.getName() + "'s Milestone");
				mile.setTag(milestone[index]);
				break;
			}
		}
		
		FontSetter.setFont(this, FontSetter.fontName.AGBReg, story);
	}
	
	public void openLink(View view)
	{
	    String id = (String) view.getTag();
	    Intent intent = getOpenYouTubeIntent(this, id);
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

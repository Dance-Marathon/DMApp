package com.uf.dancemarathon;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkProfile extends ActionBarActivity
{
	Kids kid;
	
	public MtkProfile()
	{
		// Required empty public constructor
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mtk_profile);
		
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

		pic.setImageResource(kid.getImageId(this));
		story.setText(kid.getStory());
		
		FontSetter.setFont(this, FontSetter.fontName.AGBReg, story);
	}
}

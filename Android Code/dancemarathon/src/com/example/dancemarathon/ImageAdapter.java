package com.example.dancemarathon;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @author Chris Whitten
 * This class provides the image information to the mtk grid view. See the Android developer guide for more information.
 */
public class ImageAdapter extends BaseAdapter
{
	private Context mContext;
	
	public ImageAdapter(Context c)
	{
		mContext = c;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return imageIds.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return imageIds[position];
	}
	
	public String loadJSONFromAsset() throws IOException
	{
	    String json = null;
	    try {

	        InputStream is = mContext.getAssets().open("data.json");

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;

	}
	
	private ArrayList<Kids> createArray()
	{
		ArrayList<Kids> kids_data = new ArrayList<Kids>();
		try{
			JSONArray data_arr = new JSONArray(loadJSONFromAsset());

			for(int i = 0; i < data_arr.length(); i++)
			{
				String image_name = data_arr.getJSONObject(i).getString("image");
				String story = data_arr.getJSONObject(i).getString("story");
				String name = data_arr.getJSONObject(i).getString("name");
				int age = Integer.parseInt(data_arr.getJSONObject(i).getString("ageYear"));
				
				try
				{
					Kids k = new Kids(name, age, story, image_name);
					kids_data.add(k);
				} 
				catch (ParseException e)
				{
					//Must remove this before release
					Log.d("Event Parsing", "Failed to parse event" + name);
				}
	       }
		}
		catch (IOException e)
		{
	        e.printStackTrace();
		}
		catch (JSONException e) {
	        e.printStackTrace();
		}
		
		return kids_data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ArrayList<Kids> kids = createArray();

		// Get kid's name 
		String name = kids.get(position).getName();
		
		// Get name of image for kid and adjust it to match the drawable name
		String image_name = kids.get(position).getImage_name().toLowerCase(Locale.ENGLISH);
		image_name = image_name.replace(".png", "");
		
		// Create ID of image
		int imageID = mContext.getResources().getIdentifier(image_name, "drawable", "com.example.dancemarathon");
		
		// LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View rowView = inflater.inflate(R.layout.fragment_mtk, null);
		TextView textView = new TextView(mContext);
		CircleView imageView;
		
		
        if (convertView == null) 
        {  // if it's not recycled, initialize some attributes
            textView.setText(name);
        	
        	imageView = new CircleView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 1, 1, 1);
        }
        
        else
        {
            imageView = (CircleView) convertView;
        }
        
        // Set imageView content
        imageView.setImageResource(imageID);
        
        
        // Set orange border for even positions, blue for odd
        if (position % 2 == 0)
        {
        	imageView.setBorderColor(0x99F37021);
        }
        else
        {
        	imageView.setBorderColor(0x99014083);
        }
       
        return imageView;
	}
	
	private Integer[] imageIds = {
			R.drawable.aleahn, 		R.drawable.alisonj,
			R.drawable.alysiag, 	R.drawable.alyssama,
			R.drawable.alyssamu,	R.drawable.annarose,
			R.drawable.avam, 		R.drawable.aydenm,
			R.drawable.baileyw, 	R.drawable.brightfamily,
			R.drawable.caitlynd,	R.drawable.camdena,
			R.drawable.catherinem, 	R.drawable.catrionac,
			R.drawable.cierrak, 	R.drawable.garrettl,
			R.drawable.geoffreyp, 	R.drawable.hylam,
			R.drawable.izabellan, 	R.drawable.jackm,
			R.drawable.jakec,		R.drawable.jennad,
			R.drawable.joshuaw,		R.drawable.kaedynb,
			R.drawable.kaseyv,		R.drawable.kayleee,
			R.drawable.kendalll,	R.drawable.madisong,
			R.drawable.masonh,		R.drawable.mattewc,
			R.drawable.michaels,	R.drawable.mirandal,
			R.drawable.nathanf,		R.drawable.nathanw,
			R.drawable.nickm,		R.drawable.randyg,
			R.drawable.ryant,		R.drawable.sagep,
			R.drawable.samn,		R.drawable.tylerp,
			R.drawable.tylers,		R.drawable.williamc,
			R.drawable.wyattt,		R.drawable.zanderw

	};
}

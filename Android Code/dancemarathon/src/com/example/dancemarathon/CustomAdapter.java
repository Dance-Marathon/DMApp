package com.example.dancemarathon;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author Chris Whitten This class provides the image information to the mtk
 *         grid view. See the Android developer guide for more information.
 */
public class CustomAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Kids> kids = new ArrayList<Kids>();

	public CustomAdapter (Context c) {
		mContext = c;
		kids = ParseTheKids();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.kids.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return kids.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		String image_name = this.kids.get(position).getImage_name()
				.toLowerCase(Locale.ENGLISH);
		image_name = image_name.replace(".png", "");
		int imageID = mContext.getResources().getIdentifier(image_name,
				"drawable", "com.example.dancemarathon");
		return imageID;
	}

	public String loadJSONFromAsset() throws IOException {
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

	private ArrayList<Kids> ParseTheKids() {
		
		ArrayList<Kids> kids_read = new ArrayList<Kids>();
		
		try {
			JSONArray data_arr = new JSONArray(loadJSONFromAsset());

			for (int i = 0; i < data_arr.length(); i++) 
			{
				String image_name = data_arr.getJSONObject(i)
						.getString("image");
				String story = data_arr.getJSONObject(i).getString("story");
				String name = data_arr.getJSONObject(i).getString("name");
				int age = Integer.parseInt(data_arr.getJSONObject(i).getString(
						"ageYear"));

				try
				{
					Kids k = new Kids(name, age, story, image_name);
					kids_read.add(k);
				}
				catch (ParseException e)
				{
					// Must remove this before release
					Log.d("Event Parsing", "Failed to parse event" + name);
				}
			}
		
			// Alphabetizes arraylist by name if wanted
			Collections.sort(kids_read, Kids.COMPARE_BY_NAME);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return kids_read;
	}
	
	public Kids getKid(int position)
	{
		return this.kids.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get kid's name
		String name = this.kids.get(position).getName();

		// Get name of image for kid and adjust it to match the drawable name
		String image_name = this.kids.get(position).getImage_name()
				.toLowerCase(Locale.ENGLISH);
		image_name = image_name.replace(".png", "");

		// Create ID of image
		int imageID = mContext.getResources().getIdentifier(image_name,
				"drawable", "com.example.dancemarathon");

		// LayoutInflater inflater = (LayoutInflater)
		// mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View rowView = inflater.inflate(R.layout.fragment_mtk, null);
		TextView textView = new TextView(mContext);
		CircleView imageView;

		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			textView.setText(name);

			imageView = new CircleView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(140, 140));
			//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setPadding(1, 1, 1, 1);
			
		}

		else {
			imageView = (CircleView) convertView;
			
		}
		
		// Set imageView content
		imageView.setImageResource(imageID);

		// Set orange border for even positions, blue for odd
		if (position % 2 == 0) {
			imageView.setBorderColor(mContext.getResources().getColor(R.color.dm_orange_primary));
		} 
		else
		{
			imageView.setBorderColor(mContext.getResources().getColor(R.color.dm_blue_primary));
		}

		return imageView;
	}
}

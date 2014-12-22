package com.example.dancemarathon;

import android.content.Context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView textView;
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setText("Name" + position);
        	
        	imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(170, 170));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds[position]);
        return imageView;
	}
	
	private Integer[] imageIds = {
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
			R.drawable.placeholder_pic, R.drawable.placeholder_pic,
	};
	
}
	


package com.example.dancemarathon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkFragment extends Fragment
{

	public MtkFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_mtk, container, false);
		
		//Set gridview adapter
		GridView gridview = (GridView) v.findViewById(R.id.mtk_gridview);
	    gridview.setAdapter(new ImageAdapter(this.getActivity()));
		// Inflate the layout for this fragment
		return v;
	}
	
	public static MtkFragment newInstance()
	{
		MtkFragment f = new MtkFragment();
		return f;
	}
}

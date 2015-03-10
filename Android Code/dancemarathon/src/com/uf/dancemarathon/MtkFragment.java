package com.uf.dancemarathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		final MtkAdapter adapter = new MtkAdapter(this.getActivity());
	    gridview.setAdapter(adapter);
	    gridview.setOnItemClickListener(new OnItemClickListener() 
	    {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) 
            {
            	//if (((Kids) adapter.getItem(position)).getStory().length() > 10 || ((Kids) adapter.getItem(position)).getYoutube_id().length() > 2)
            	//{           	
            		Intent intent = new Intent(getActivity(), MtkProfile.class);
            		Bundle b = new Bundle();
            		b.putParcelable("kid", (Kids)adapter.getItem(position));
            		intent.putExtras(b);
            		startActivity(intent);
            	//}
            }
	    });
	    
		// Inflate the layout for this fragment
		return v;
	}
	
	        
	 public static MtkFragment newInstance()
	 {
		 MtkFragment f = new MtkFragment();
		 return f;
	 }
	
}
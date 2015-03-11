package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
	private GridView gridview;
	private MtkAdapter adapter;
	private Context mActivity;
	private KidsLoader loader;
	private boolean updatePending = false;
	public MtkFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_mtk, container, false);
		gridview = (GridView) v.findViewById(R.id.mtk_gridview);
	    
		gridview.setOnItemClickListener(new OnItemClickListener() 
	    {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) 
            {
            	if (((Kids) parent.getItemAtPosition(position)).getStory().length() > 10 || ((Kids) parent.getItemAtPosition(position)).getYoutube_id().length() > 2)
            	{           	
            		Intent intent = new Intent(getActivity(), MtkProfile.class);
            		Bundle b = new Bundle();
            		b.putParcelable("kid", (Kids)parent.getItemAtPosition(position));
            		intent.putExtras(b);
            		startActivity(intent);
            	}
            }
	    });
		// Inflate the layout for this fragment
		return v;
	}
	
	
	        
	 /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//If the async task finished before the view was created, update adapter
		if(updatePending)
		{
			gridview.setAdapter(adapter);
		}
				
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		loader.cancel(true);
		super.onDestroy();
	}

	public static MtkFragment newInstance(Context c)
	 {
		 MtkFragment f = new MtkFragment();
		 f.mActivity = c;
		 f.loadKids();
		 return f;
	 }
	
	private void loadKids()
	{
		//Set gridview adapter
		loader = new KidsLoader();
		loader.execute();
	}
	 
	 private class KidsLoader extends AsyncTask<Void, Void, Void>
	 {
		@Override
		protected Void doInBackground(Void... params) {
			adapter = new MtkAdapter(mActivity);
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			
			if(gridview != null)
				gridview.setAdapter(adapter);
			else
				updatePending = true;
			
		}
		
		
		
		
		 
	 }
	
}
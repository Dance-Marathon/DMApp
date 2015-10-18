package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.uf.dancemarathon.FontSetter.fontName;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity shows FAQs in a listview
 * @author Chris Whitten
 *
 */
public class FAQActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		
		//Get the listview
		ListView list = (ListView) findViewById(R.id.faq_list);
		
		//Get the faqs
		try {
			ArrayList<FAQ> faqs = parseJSONData("faq.json");
			list.setAdapter(new faqAdapter(this, faqs));
			list.setClickable(false);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			displayErrorToast();
			list.setVisibility(View.GONE);
			//e.printStackTrace();
		}
		
		//Set action bar title and color
		ActionBar bar = getSupportActionBar();
		bar.setTitle("FAQs");

		int color = getResources().getColor(R.color.action_bar_color);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
	}

	protected void onStart()
	{
		super.onStart();
		
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "FAQActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.FAQ_ACTIVITY_NAME);
		}
	}
	
	/**
	 * This method displays an error toast
	 */
	private void displayErrorToast()
	{
		Toast toast = Toast.makeText(this, "Could not display FAQ Page", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This class serves as an adapter for FAQ objects. 
	 * It is used to populate the listview.
	 * @author Chris Whitten
	 *
	 */
	private class faqAdapter extends ArrayAdapter<FAQ>
	{
		private Context c;
		private ArrayList<FAQ> faqs;
		
		public faqAdapter(Context c, ArrayList<FAQ> faqs)
		{
			super(c, R.layout.faq_list_item, faqs);
			this.faqs = faqs;
			this.c = c;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return faqs.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getItem(int)
		 */
		@Override
		public FAQ getItem(int position) {
			// TODO Auto-generated method stub
			return faqs.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getPosition(java.lang.Object)
		 */
		@Override
		public int getPosition(FAQ item) {
			// TODO Auto-generated method stub
			return faqs.indexOf(item);
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if(convertView == null)
			{
				convertView = inflater.inflate(R.layout.faq_list_item, parent, false);
			}
			
			setFAQDetails(convertView, faqs.get(position));
			return convertView;
		}
		
		/**
		 * Set the textviews for the given view using the input FAQ object
		 * @param v The view representing the entire item in the list
		 * @param f The FAQ object
		 */
		private void setFAQDetails(View v, FAQ f)
		{
			TextView question = (TextView) v.findViewById(R.id.faq_item_question);
			TextView answer = (TextView) v.findViewById(R.id.faq_item_answer);
			
			FontSetter.setFont(c, fontName.AGBMed, question);
			FontSetter.setFont(c, fontName.AGBReg, answer);
			
			question.setText(f.question);
			answer.setText(f.answer);
		}
		
	}
	
	
	/**
	 * Parses the given file for FAQ objects
	 * @param fileName The file to use
	 * @return The list of FAQ objects
	 * @throws JSONException
	 * @throws IOException
	 */
	private ArrayList<FAQ> parseJSONData(String fileName) throws JSONException, IOException
	{
		ArrayList<FAQ> faqs = new ArrayList<FAQ>();
		String json="";
		String next="";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open(fileName)));
		while((next=reader.readLine()) != null)
			json+=next;
		reader.close();
		
		if(json.length() > 1)
		{
			//Log.d("q", "here");
			JSONArray arr = new JSONArray(json);
			for(int i=0; i < arr.length(); i++)
			{
				JSONObject o = (JSONObject) arr.get(i);
				String question = o.getString("Question");
				String answer = o.getString("Answer");
				//Log.d("q", question);
				faqs.add(new FAQ(question, answer));
			}
		}
			
		return faqs;
	}
	
	/**
	 * This class represents an FAQ
	 * @author Chris Whitten
	 *
	 */
	private class FAQ {
		public String question;
		public String answer;
		
		public FAQ(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
		
	}
}

package com.example.dancemarathon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Chris Whitten
 * This class represents a DM event.
 */
public class Event
{
	private String id;
	/**
	 * The title of the event
	 */
	private String title;
	/**
	 * The name or address of the location of the event
	 */
	private String location;
	private final String timeStampFormat = "yyyy-MM-dd HH:mm:ss";
	/**
	 * A timestamp of the start date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_startDate;
	/**
	 * A timestamp of the end date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_endDate;
	/**
	 * A timestamp of the last modified date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_lastMod;
	
	//The associated Date objects for the above timestamps
	private Date startDate;
	private Date endDate;
	@SuppressWarnings("unused")
	private Date lastMod;
	
	/**
	 * A description of the event
	 */
	private String description; 

	/**
	 * @param id The event id as specified on the server
	 * @param title The title of the event
	 * @param location The name or address of the location of the event
	 * @param t_startDate The startDate in string format
	 * @param t_endDate The endDate in string format
	 * @param t_lastMod The last modified date in string format
	 * @param description The description of the event
	 * @throws ParseException if the dates where unable to be parsed
	 */
	public Event(String id, String title, String location, String t_startDate,
			String t_endDate, String t_lastMod, String description) throws ParseException 
	{
		this.id = id;
		this.title = title;
		this.location = location;
		this.t_startDate = t_startDate;
		this.t_endDate = t_endDate;
		this.t_lastMod = t_lastMod;
		this.description = description;
		
		parseTimeStamps();
	}


	
	protected String getAMPMHourVal()
	{
		String timeText = t_startDate.substring(t_startDate.length()-8, t_startDate.length()-3);
		int hour = Integer.parseInt(timeText.split(":")[0]);
		hour = hour % 12;
		if(hour == 0)
			hour = 12;
		return Integer.toString(hour);
	}
	/**
	 * This method will parse the timestamps of the event to give values to the {@link Date} objects
	 */
	private void parseTimeStamps() throws ParseException
	{
		//This object allows us to parse a timestamp into a Date object
		SimpleDateFormat df = new SimpleDateFormat(timeStampFormat, Locale.US);
		startDate = df.parse(t_startDate);
		endDate = df.parse(t_endDate);
		lastMod = df.parse(t_lastMod);
	}
	
	/**
	 * @return the id
	 */
	protected String getId()
	{
		return id;
	}


	/**
	 * @return the title
	 */
	protected String getTitle()
	{
		return title;
	}


	/**
	 * @return the location
	 */
	protected String getLocation()
	{
		return location;
	}


	/**
	 * @return the t_startDate
	 */
	protected String getT_startDate()
	{
		return t_startDate;
	}


	/**
	 * @return the t_endDate
	 */
	protected String getT_endDate()
	{
		return t_endDate;
	}


	/**
	 * @return the t_lastMod
	 */
	protected String getT_lastMod()
	{
		return t_lastMod;
	}


	/**
	 * @return the description
	 */
	protected String getDescription()
	{
		return description;
	}


	/**
	 * @param id the id to set
	 */
	protected void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @param title the title to set
	 */
	protected void setTitle(String title)
	{
		this.title = title;
	}


	/**
	 * @param location the location to set
	 */
	protected void setLocation(String location)
	{
		this.location = location;
	}


	/**
	 * @param t_startDate the t_startDate to set
	 * @throws ParseException if the date was unable to be parsed
	 */
	protected void setT_startDate(String t_startDate) throws ParseException
	{
		this.t_startDate = t_startDate;
		parseTimeStamps();
	}


	/**
	 * @param t_endDate the t_endDate to set
	 */
	protected void setT_endDate(String t_endDate)
	{
		this.t_endDate = t_endDate;
	}


	/**
	 * @param t_lastMod the t_lastMod to set
	 */
	protected void setT_lastMod(String t_lastMod)
	{
		this.t_lastMod = t_lastMod;
	}


	/**
	 * @param description the description to set
	 */
	protected void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * This method gets the integer representation of the month of either the start or end date.
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return Integer represenation of the month for start or end date
	 */
	public int getMonth(boolean useStartDate)
	{
		Calendar c = Calendar.getInstance();
		
		if(useStartDate)
			c.setTime(startDate);
		else
			c.setTime(endDate);
		
		return c.get(Calendar.MONTH);
	}
	
	/**
	 * 
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return 3-character string representation of the month
	 */
	public String getMonthText(boolean useStartDate)
	{
		int month = getMonth(useStartDate);
		
		switch(month)
		{
		case 1: return "Jan";
		case 2: return "Feb";
		case 3: return "Mar";
		case 4: return "Apr";
		case 5: return "May";
		case 6: return "Jun";
		case 7: return "Jul";
		case 8: return "Aug";
		case 9: return "Sep";
		case 10: return "Oct";
		case 11: return "Nov";
		case 12: return "Dec";
		default: return "Nul";
		}
	}
	
	/**
	 * This method gets the integer representation of the day of either the start or end date.
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return Integer represenation of the day for start or end date
	 */
	public int getDay(boolean useStartDate)
	{
		Calendar c = Calendar.getInstance();
		
		if(useStartDate)
			c.setTime(startDate);
		else
			c.setTime(endDate);
		
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * This method gets the string representation of the AM or PM value of either the start or end date.
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return String represenation of the AM or PM value for start or end date
	 */
	public String getAMOrPM(boolean useStartDate)
	{
		Calendar c = Calendar.getInstance();
		
		if(useStartDate)
			c.setTime(startDate);
		else
			c.setTime(endDate);
		
		int val =  c.get(Calendar.AM_PM);
		
		if(val == Calendar.AM)
			return "AM";
		else 
			return "PM";
	}
	
	public String toString()
	{
		String rep = "";
		rep = rep.concat(getTitle() + "\t");
		rep = rep.concat(getLocation() + "\t");
		rep = rep.concat(getT_startDate() + "\t");
		
		return rep;
	}
}

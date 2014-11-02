package com.example.dancemarathon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public String toString()
	{
		String rep = "";
		rep = rep.concat(getTitle() + "\t");
		rep = rep.concat(getLocation() + "\t");
		rep = rep.concat(getT_startDate() + "\t");
		
		return rep;
	}
}

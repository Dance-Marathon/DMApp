package com.example.dancemarathon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents an announcement
 * @author Chris Whitten
 *
 */
public class Announcement 
{
	/**
	 * The announcement text
	 */
	public String text;
	/**
	 * The date the announcement was sent on
	 */
	private Date date;
	
	public Announcement(String text, String date, String dateFormat) throws ParseException{
		this.text = text;
		setDate(date, dateFormat);
	}
	
	/**
	 * This method parses the input string to create set this object's date
	 * @param date The string date
	 * @param dateFormat The format the date is in. See {@link SimpleDateFormat}
	 * @throws ParseException If the date could not be parsed
	 */
	public void setDate(String date, String dateFormat) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
		this.date = df.parse(date);
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	
	
	
}

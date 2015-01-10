package com.example.dancemarathon;

import java.text.ParseException;
import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class Kids implements Parcelable
{
	private String name;
	private int age;
	private String story;
	private String image_name;
	
	public Kids(String name, int age, String story, String image_name) throws ParseException 
	{
		this.name = name;
		this.age = age;
		this.story = story;
		this.image_name = image_name;
	}
	
	public Kids()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		dest.writeString(this.name);
		dest.writeInt(this.age);
		dest.writeString(this.story);
	}
	
	protected String getName()
	{
		return name;
	}
	
	protected int getAge()
	{
		return age;
	}
	
	protected String getStory()
	{
		return story;
	}
	
	protected String getImage_name()
	{
		return image_name;
	}
	
	protected void setName(String name)
	{
		this.name = name;
	}
	
	protected void setAge(int age)
	{
		this.age = age;
	}
	
	protected void setStory(String story)
	{
		this.story = story;
	}
	
	protected void setImage_name(String image_name)
	{
		this.image_name = image_name;
	}
	
	public static Comparator<Kids> COMPARE_BY_NAME = new Comparator<Kids>() {
        public int compare(Kids one, Kids other) {
            return one.getName().compareTo(other.getName());
        }
    };
	
}
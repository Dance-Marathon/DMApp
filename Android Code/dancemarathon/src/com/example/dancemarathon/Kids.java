package com.example.dancemarathon;

public class Kids
{
	private String name;
	private int age;
	private String story;
	private String image_name;
	private String thumb;
	
	public Kids(String name, int age, String story, String image_name, String thumb)
	{
		this.name = name;
		this.age = age;
		this.story = story;
		this.image_name = image_name;
		this.thumb = thumb;
	}
	
	public Kids()
	{
		// TODO Auto-generated constructor stub
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
	
	
}
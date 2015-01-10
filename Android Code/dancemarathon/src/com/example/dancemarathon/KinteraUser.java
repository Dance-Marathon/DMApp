package com.example.dancemarathon;

import java.io.Serializable;

import android.os.Parcel;

import android.os.Parcelable;

/**
 * @author Chris
 * This class represents the kintera user
 */
public class KinteraUser implements Parcelable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public String userName;
	public String realName;
	public String pageURL;
	private String password;
	public double fundGoal;
	public double fundRaised;

	public KinteraUser(String userName, String password, String realName, double fundGoal, double fundRaised, String pageURL)
	{
		this.userName = userName;
		this.setPassword(password);
		this.realName = realName;
		this.fundGoal = fundGoal;
		this.fundRaised = fundRaised;
		this.pageURL = pageURL;
	}

	public KinteraUser()
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
		dest.writeString(userName);
		dest.writeString(password);
		dest.writeString(realName);
		dest.writeString(pageURL);
		dest.writeDouble(fundGoal);
		dest.writeDouble(fundRaised);
	}
	
	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	private KinteraUser(Parcel in)
	{
		this.userName = in.readString();
		this.password = in.readString();
		this.realName = in.readString();
		this.pageURL = in.readString();
		this.fundGoal = in.readDouble();
		this.fundRaised = in.readDouble();
	}
	
	public static final Parcelable.Creator<KinteraUser> CREATOR
    		= new Parcelable.Creator<KinteraUser>() {
		
		public KinteraUser createFromParcel(Parcel in) {
		    return new KinteraUser(in);
		}
		
		public KinteraUser[] newArray(int size) {
		    return new KinteraUser[size];
		}
};
	
}
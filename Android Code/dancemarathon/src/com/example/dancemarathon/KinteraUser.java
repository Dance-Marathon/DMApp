package com.example.dancemarathon;

import android.os.Parcel;

import android.os.Parcelable;

/**
 * @author Chris
 * This class represents the kintera user
 */
public class KinteraUser implements Parcelable
{
	public String userName;
	public String realName;
	public String pageURL;
	public double fundGoal;
	public double fundRaised;
	
	public KinteraUser(String userName, String realName, double fundGoal, double fundRaised, String pageURL)
	{
		this.userName = userName;
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
		dest.writeString(realName);
		dest.writeString(pageURL);
		dest.writeDouble(fundGoal);
		dest.writeDouble(fundRaised);
	}
	
	private KinteraUser(Parcel in)
	{
		this.userName = in.readString();
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

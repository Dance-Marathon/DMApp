package com.example.dancemarathon;

/**
 * @author Chris
 * This class represents the kintera user
 */
public class KinteraUser
{
	public String userName;
	public String realName;
	public String teamName;
	public double teamGoal;
	public double teamRaised;
	public double fundGoal;
	public double fundRaised;
	
	public KinteraUser(String userName, String realName, String teamName,
			double teamGoal, double teamRaised, double fundGoal,
			double fundRaised)
	{
		this.userName = userName;
		this.realName = realName;
		this.teamName = teamName;
		this.teamGoal = teamGoal;
		this.teamRaised = teamRaised;
		this.fundGoal = fundGoal;
		this.fundRaised = fundRaised;
	}
}

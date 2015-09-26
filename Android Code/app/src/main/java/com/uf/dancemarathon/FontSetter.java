package com.uf.dancemarathon;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontSetter 
{
	public static String AGBBol = "AGBookRouCFFBol.otf";
	public static String AGBMed = "AGBookRouCFFMed.otf";
	public static String AGBReg = "AGBookRouCFFReg.otf";
	public static String ALTB = "AvenirLTStd-Black.ttf";
	public static String ALTL = "AvenirLTStd-Light.ttf";
	public static String ALTM = "AvenirLTStd-Medium.ttf";
	public static String ALTMO = "AvenirLTStd-MediumOblique.ttf";
	public static String ALTR = "AvenirLTStd-Roman.ttf";
	public static String P = "Pacifico.ttf";
	
	public enum fontName
	{
		AGBBol, AGBMed, AGBReg, ALTB, ALTL, ALTM, ALTMO, ALTR, P
	}
	
	public static void setFont(Context c, fontName f, TextView...params)
	{
		for(int index = 0; index < params.length; index++)
		{
			TextView curr = params[index];
			
			switch(f)
			{
			case AGBBol: 
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + AGBBol));
				break;
			case AGBMed:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + AGBBol));
				break;
			case AGBReg:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + AGBReg));
				break;
			case ALTB:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + ALTB));
				break;
			case ALTL:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + ALTL));
				break;
			case ALTM:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + ALTM));
				break;
			case ALTMO:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + ALTMO));
				break;
			case ALTR:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + ALTR));
				break;
			case P:
				curr.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/" + P));
				break;
			default:
				break;

			}
			
		}
	}
	
}

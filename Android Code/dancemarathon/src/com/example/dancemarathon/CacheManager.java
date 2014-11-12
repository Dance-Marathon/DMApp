package com.example.dancemarathon;

import java.io.File;

import android.content.Context;

/**
 * @author Chris Whitten
 * This class is responsible for reading and writing to cache files on the Android system
 * as well as managing cache space.
 */
public class CacheManager
{
	Context context;
	
	public CacheManager(Context c)
	{
		context = c;
	}
	
	public boolean checkIfFileExists(String fileName)
	{
		File cacheDir = context.getCacheDir();
		String[] cacheFileNames = cacheDir.list();
		
		for(int i = 0; i< cacheFileNames.length; i++)
		{
			String currFile = cacheFileNames[i];
			if(currFile.equals(fileName))
				return true;
		}
		
		return false;
	}
	
}

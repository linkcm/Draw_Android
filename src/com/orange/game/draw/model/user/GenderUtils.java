package com.orange.game.draw.model.user;

import com.orange.game.R;

import android.content.Context;

public class GenderUtils {

	public static final String MALE = "m";
	public static final String FEMALE = "f";
	
	public static boolean valueOf(int genderIntValue){
		return (genderIntValue == 1);
	}
	
	public static String getUserGender(boolean gender,Context context)
	{
		String genderString = "";
		if (gender)
		{
			genderString = context.getString(R.string.male);
		}else {
			genderString = context.getString(R.string.female);
		}
		return genderString;
	}

	public static boolean boolFromString(String genderString)
	{
		if (genderString == null)
			return false;
		
		if (genderString.equalsIgnoreCase(MALE)){
			return true;
		}
		
		return false;
	}

	public static String toString(boolean gender) {
		if (gender)
			return MALE;
		else {
			return FEMALE;
		}
	}
	
}

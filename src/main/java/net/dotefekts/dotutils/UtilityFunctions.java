package net.dotefekts.dotutils;

public class UtilityFunctions {

	public static String joinArray(String[] arr, String seperator){
		String result = "";
		for(String str : arr)
			if(result.isEmpty())
				result = str;
			else
				result = result + seperator + str;
		return result;
	}
}

package utility;

public class StringUtility {

	public static String getLastPart(String string, char delimiter) {
		int delimiterLastIndex = string.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : string.substring(delimiterLastIndex+1);
	}
	
	public static String getLastPart(String string, String delimiter) {
		int delimiterLastIndex = string.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : string.substring(delimiterLastIndex+1);
	}
}
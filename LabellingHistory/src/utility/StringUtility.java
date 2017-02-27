package utility;

public class StringUtility {

	public static String getLastPartAfterDelimiter(String string, char delimiter) {
		int delimiterLastIndex = string.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : string.substring(delimiterLastIndex+1);
	}
	
	public static String getLastPartAfterDelimiter(String string, String delimiter) {
		int delimiterLastIndex = string.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : string.substring(delimiterLastIndex+1);
	}
	
	public static String removeLastPartAfterDelimiter(String string, String delimiter) {
		int delimiterLastIndex = string.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? string : string.substring(0, delimiterLastIndex);
	}
	
	public static void main(String[] args) {
		System.out.println(removeLastPartAfterDelimiter("askdasdasds", "das"));
	}
}


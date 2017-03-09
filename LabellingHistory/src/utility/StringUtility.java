package utility;

public class StringUtility {

	public static String getLastPartAfterDelimiter(String str, char delimiter) {
		int delimiterLastIndex = str.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : str.substring(delimiterLastIndex+1);
	}
	
	public static String getLastPartAfterDelimiter(String str, String delimiter) {
		int delimiterLastIndex = str.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? null : str.substring(delimiterLastIndex+1);
	}
	
	public static String removeLastPartAfterDelimiter(String str, String delimiter) {
		int delimiterLastIndex = str.lastIndexOf(delimiter);
		return (delimiterLastIndex == -1) ? str : str.substring(0, delimiterLastIndex);
	}
	
	public static String removeLastCharacter(String str) {
		return str.substring(0, str.length() - 1);
	}
	
	public static String removeLastCharacter(StringBuilder sb) {
		return sb.substring(0, sb.length() - 1);
	}
	
	public static void main(String[] args) {
		System.out.println(removeLastPartAfterDelimiter("askdasdasds", "das"));
	}
}


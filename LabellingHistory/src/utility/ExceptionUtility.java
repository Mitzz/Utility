package utility;

public class ExceptionUtility {

	public static void throwIfAnyNull(String message, Object ... objects){
		for(Object obj: objects)
			if(obj == null) throw new NullPointerException(message);
	}
	
	public static void throwIfAnyNull(Object ... objects){
		throwIfAnyNull("Null Pointer Exception", objects);
	}
	
	public static void throwIllegalArgument(String message) throws IllegalArgumentException{
		throw new IllegalArgumentException(message);
	}
}

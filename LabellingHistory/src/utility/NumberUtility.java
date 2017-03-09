package utility;

public class NumberUtility {
	public static int keepWithinUpperRange(int num, int upperRange) {
		if (num > upperRange)
			num = upperRange;
		return num;
	}
}

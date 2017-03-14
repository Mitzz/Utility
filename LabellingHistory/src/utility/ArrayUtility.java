package utility;

import utility.misc.RandomRange;

public class ArrayUtility {

	public static void populateArrayRandomlyWithRange(int[] arr, int lo, int hi) {
		RandomRange randomRange = new RandomRange(lo, hi);
		for(int i = 0; i < arr.length; i++)
			arr[i] = randomRange.get();
	}
}

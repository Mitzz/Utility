package utility.misc;

public class RandomRange{
	private int lo;
	private int hi;
	
	public RandomRange(int lo, int hi) {
		this.lo = lo;
		this.hi = hi + 1;
	}
	
	public int get(){
		return (int)(Math.random() * (hi - lo) + lo);
	}
	
}
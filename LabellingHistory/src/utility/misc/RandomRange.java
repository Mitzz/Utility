package utility.misc;

public class RandomRange{
	private int lo;
	private int hi;
	private int offset;
	
	public RandomRange(int lo, int hi) {
		if(lo >= hi) 
			throw new IllegalArgumentException("Lower Range < Higher Range not satisfied");
		this.lo = lo;
		this.hi = hi + 1;
		this.offset = 1;
	}
	
	public RandomRange(int lo, int hi, int offset){
		this(lo, hi);
		this.offset = offset;
	}
	
	public int get(){
		int no = (int)(Math.random() * (hi - lo) + lo);
		if(!isEqualToHi(no) && isOffset()){
			no = no - (no % offset) + (lo % offset);
		}
		return no;
	}
	
	private boolean isEqualToHi(int no){
		return no == hi - 1;
	}
	
	private boolean isOffset(){
		return offset > 1;
	}
	
	public static void main(String[] args) {
		int lo = 10;
		int hi = 40;
		int offset = 4;
		RandomRange range = new RandomRange(lo, hi, offset);
		int count = 0;
		while(true){
			count++;
			int no = range.get();
			System.out.println(no);
			if(no == hi){
				System.out.println("Count = " + count);
				System.out.println(no);
				break;
			}
		}
	}
}

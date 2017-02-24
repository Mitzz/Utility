package algo;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> q = new RandomizedQueue<String>();
		while(!StdIn.isEmpty()){
			String item = StdIn.readString();
			if(k > 0) {
				if(k == q.size())
					StdOut.print(q.dequeue());
				q.enqueue(item);
			}
		}
	}
}

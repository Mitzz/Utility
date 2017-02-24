package algo;

import edu.princeton.cs.algs4.StdRandom;

public class Mergesort {
	
	public static final int CUT_OFF = 7;
	
	public static void sort(Comparable[] a){
		Comparable[] aux = new Comparable[a.length];
		sort(a, aux, 0, a.length - 1);
	}

	private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
		if(hi <= lo + CUT_OFF - 1){
			Insertion.sort(a, lo, hi);
			return ;
		}
		
		int mid = (hi + lo)/2;
		
		sort(a, aux, lo, mid);
		sort(a, aux, mid + 1, hi);

		merge(a, aux, lo, mid, hi);
	}

	public static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi){
		
		for(int k = lo; k <= hi; k++)
			aux[k] = a[k]; 
		
		int i = lo;
		int j = mid + 1;
		
		for(int k = lo; k <= hi; k++){
			if(i > mid) 					a[k] = aux[j++];
			else if(j > hi) 				a[k] = aux[i++];
			else if(AlgoUtility.less(aux[j], aux[i])) 	a[k] = aux[j++];
			else							a[k] = aux[i++];	
		}
	}
	
	public static void main(String[] args) {
		Integer[] a = new Integer[40000000];
		
		for(int i = 0; i < a.length; i++)
			a[i] = StdRandom.uniform(0, i + 1);

		System.out.println("-");
		sort(a);
		System.out.println(AlgoUtility.isSorted(a));
	}
}

class AlgoUtility{
	public static boolean less(Comparable v, Comparable w){
		return v.compareTo(w) < 0;
	}
	
	public static boolean isSorted(Comparable[] a){
		return isSorted(a, 0, a.length - 1);
	}
	
	public static boolean isSorted(Comparable[] a, int lo, int hi){
		for(int i = lo; i <= hi - 1; i++)
			if(less(a[i + 1], a[i]))
				return false;
		return true;
	}
	
	public static void exch(Comparable[] a, int j, int i) {
		Comparable temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

}

class Insertion{
	
	public static void sort(Comparable[] a){
		sort(a, 0, a.length - 1);
	}
	
	public static void sort(Comparable[] a, int lo, int hi){
		for(int i = lo; i <= hi; i++)
			for(int j = i; j >= lo + 1; j--)
				if(AlgoUtility.less(a[j], a[j - 1]))
					AlgoUtility.exch(a, j, j - 1);
	}
	
	public static void main(String[] args) {

		Integer[] a = new Integer[1000];
		
		for(int i = 0; i < a.length; i++)
			a[i] = StdRandom.uniform(0, i + 1);

		System.out.println("-");
		sort(a);
		System.out.println(AlgoUtility.isSorted(a));

	}
}
package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import oracle.net.aso.l;

public class CollectionUtility {

	public static <T> String join(Collection<T> collection, String delimiter) {

	    StringBuilder sb = new StringBuilder();

	    String loopDelimiter = "";

	    for(T element : collection) {

	        sb.append(loopDelimiter);
	        sb.append(element);            

	        loopDelimiter = delimiter;
	    }

	    return sb.toString();
	}
	
	public static <T> Collection<T> add(Collection<T> collection, T ... element) {
	    for(T elem : element) {
	        collection.add(elem);            
	    }
	    return collection;
	}
	
	public static <T> SortedSet<T> createSortedSet(T ... element) {
		SortedSet<T> set = new TreeSet<T>();
	    for(T elem : element) {
	        set.add(elem);            
	    }
	    return set;
	}
	
	public static <T> boolean isNonEmpty(Collection<T> col) {
		return (col != null && col.size() != 0);
	}
	
	public static <T> boolean isNonEmpty(T[] array) {
		return (array != null && array.length != 0);
	}
	
	public static <T> Set<T> getSetFromArray(T[] array){
		if(isNonEmpty(array))
			return new HashSet<T>(Arrays.asList(array));
		else
			throw new IllegalArgumentException("Set To Array Conversion Error due to empty array");
	}
	
	public static <T> T[] removeLastElement(T[] array){
		if(isNonEmpty(array)){
			resizeArray(array, array.length - 1);
			return array;
		}
		else throw new IllegalArgumentException("Empty array");
	}

	private static <T> void resizeArray(T[] array, int size) {
		T[] copy = (T[]) new Object[size];
		for(int i = 0; i < size; i++)
			copy[i] = array[i];
		
		array = copy;
	}
	
	public static <T> List<List<T>> splitCollectionToList(Collection<T> original, int partitionSize){
		List<T> originalList = new ArrayList<T>(original);
		List<List<T>> partitionsList = new LinkedList<List<T>>();
		for (int i = 0; i < original.size(); i += partitionSize) {
		    partitionsList.add(originalList.subList(i,i + Math.min(partitionSize, original.size() - i)));
		}
		return partitionsList;
		
		
	}
	
	public static void display(Object[] a){
		display(a, 0, a.length - 1);
	}
	
	public static void display(Object[] a, int lo, int hi){
		for(int i = lo; i <= hi; i++)
			System.out.println(a[i]);
	}

	public static <T> void displayList(List<T> list, String delimiter) {
		for(T elem : list)
			System.out.println(elem + delimiter);
	}
}

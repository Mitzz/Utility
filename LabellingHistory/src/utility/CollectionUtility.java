package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import db.DbUtility;
import utility.misc.RandomRange;

public class CollectionUtility {

	public static <T> String join(Collection<T> collection, String delimiter) {

		StringBuilder sb = new StringBuilder();

		for (T element : collection) {
			sb.append(element);
			sb.append(delimiter);
		}

		return StringUtility.removeLastPartAfterDelimiter(sb.toString(), delimiter);
	}

	public static <T> Collection<T> add(Collection<T> collection, T... element) {
		if (isNonEmpty(collection) && isNonEmpty(element)) {
			for (T elem : element) {
				collection.add(elem);
			}
			return collection;
		} else {
			throw new IllegalArgumentException("Empty Collection or Elements");
		}
	}

	public static <T> SortedSet<T> createSortedSet(T... elements) {
		if (elements != null) {
			SortedSet<T> set = new TreeSet<T>();
			for (T elem : elements) {
				set.add(elem);
			}
			return set;
		} else {
			throw new IllegalArgumentException("Empty Elements");
		}
	}

	public static <T> boolean isNonEmpty(Collection<T> col) {
		return (col != null && col.size() != 0);
	}

	public static <T> boolean isNonEmpty(T[] array) {
		return (array != null && array.length != 0);
	}

	public static <T> Set<T> getSetFromArray(T[] array) {
		if (isNonEmpty(array))
			return new HashSet<T>(Arrays.asList(array));
		else
			throw new IllegalArgumentException("Set To Array Conversion Error due to empty array");
	}

	public static <T> T[] removeLastElement(T[] array) {
		if (isNonEmpty(array)) {
			resizeArray(array, array.length - 1);
			return array;
		} else
			throw new IllegalArgumentException("Empty array");
	}

	private static <T> void resizeArray(T[] array, int size) {
		if (isNonEmpty(array)) {
			T[] copy = (T[]) new Object[size];
			for (int i = 0; i < size; i++)
				copy[i] = array[i];

			array = copy;
		} else
			throw new IllegalArgumentException("Empty array");
	}

	public static <T> List<List<T>> splitCollectionToList(Collection<T> original, int partitionSize) {
		if (isNonEmpty(original) && partitionSize > 0) {
			List<T> originalList = new ArrayList<T>(original);
			List<List<T>> partitionsList = new LinkedList<List<T>>();
			for (int i = 0; i < original.size(); i += partitionSize) {
				partitionsList.add(originalList.subList(i, i + Math.min(partitionSize, original.size() - i)));
			}
			return partitionsList;
		} else
			throw new IllegalArgumentException("Collection must be non-Empty or partition size must be greater than zero");

	}

	public static void display(Object[] a) {
		if (isNonEmpty(a))
			displayBottomUp(a, 0, a.length - 1);
		else
			throw new IllegalArgumentException("Empty Array");

	}

	public static void displayBottomUp(Object[] a, int lo, int hi) {
		if (isNonEmpty(a) && lo > 0 && hi > 0 && hi >= lo) {
			for (int i = lo; i <= hi; i++)
				System.out.println(a[i]);
		} else
			throw new IllegalArgumentException("Empty array or (" + lo + "," + hi + ") not in proper range");
	}

	public static void displayTopDown(Object[] a, int lo, int hi) {
		if (isNonEmpty(a) && lo > 0 && hi > 0 && hi >= lo) {
			for (int i = hi; i >= lo; i--)
				System.out.println(a[i]);
		} else
			throw new IllegalArgumentException("Empty array or (" + lo + "," + hi + ") not in proper range");
	}

	public static <T> void displayCollection(Collection<T> col, String delimiter) {
		if (isNonEmpty(col)) {
			for (T elem : col)
				System.out.print(elem + delimiter);
		} else
			throw new IllegalArgumentException("Empty List");
	}

	public static <T> void displayCollection(Collection<T> col) {
		if (isNonEmpty(col)) {
			for (T elem : col)
				System.out.println(elem);
		} else
			throw new IllegalArgumentException("Empty List");
	}

	public static void populateArrayWithRange(int[] arr, int lo, int hi) {
		RandomRange randomRange = new RandomRange(lo, hi);
		for (int i = 0; i < arr.length; i++)
			arr[i] = randomRange.get();
	}

	public static <T> void serialize(Collection<T> al, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(al);
		} finally {
			try {
				DbUtility.closeResources(oos, fos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static <T> Collection<T> deserialize(String fileName) throws IOException, ClassNotFoundException {
		Collection<T> col = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			col = (Collection<T>) ois.readObject();

			for (T elem : col) {
				System.out.println(elem);
			}
			return col;
		} finally {

			try {
				DbUtility.closeResources(ois, fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) {
		String fileName = "C:\\Users\\mithul.bhansali\\Desktop\\list.ser";
		
		try {
//			serialize(Arrays.asList("1", "2", "3", "4"), fileName);
			deserialize(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

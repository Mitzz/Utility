package algo;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N;
	private Item[] items;

	public RandomizedQueue() {
		items = (Item[]) new Object[1];
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return N;
	}

	public void enqueue(Item item) {
		check(item);
		if (N == items.length)
			resize(N * 2);
		items[N] = item;
		N++;
	}

	private void resize(int size) {
		Item[] copy = (Item[]) new Object[size];

		for (int i = 0; i < N; i++)
			copy[i] = items[i];

		items = copy;
	}

	public Item dequeue() {
		checkEmpty();
		int index = StdRandom.uniform(N);
		Item item = items[index];
		items[index] = items[--N];
		
		if (N != 0 && N == items.length / 4)
			resize(N * 2);
		return item;
	}

	public Item sample() {
		checkEmpty();
		return items[StdRandom.uniform(N)];
	}

	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private int size = N;
			private Item[] elements;
			
			{
				if(N != 0){
					elements = (Item[])new Object[N];
					for(int i = 0; i < N; i++)
						elements[i] = items[i];
					StdRandom.shuffle(elements, 0, N);
				}
			}

			@Override
			public boolean hasNext() {
				return size != 0;
			}

			@Override
			public Item next() {
				if (size == 0)
					throw new NoSuchElementException();
				Item item = elements[--size];
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static void main(String[] args) {
		
		RandomizedQueue<String> q = new RandomizedQueue<String>();
		
		q.enqueue("M");
		q.enqueue("I");
		q.dequeue();
		q.dequeue();
		q.enqueue("T");
		q.dequeue();
		q.enqueue("U");
		q.enqueue("L");
		
		for(String str: q)
			System.out.println(str);
		System.out.println("***");
		
		for(String str: q)
			System.out.println(str);


		System.out.println("-----------");
		
//		System.out.println(q.dequeue());
//		System.out.println(q.dequeue());
//		System.out.println(q.dequeue());
//		System.out.println(q.dequeue());
//		System.out.println(q.dequeue());
//		System.out.println(q.dequeue());
//		
	}

	private void check(Item item) {
		if (item == null)
			throw new NullPointerException();
	}

	private void checkEmpty() {
		if (isEmpty())
			throw new NoSuchElementException();
	}

}

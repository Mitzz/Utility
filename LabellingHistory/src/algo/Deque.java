package algo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private Node first;
	private Node last;
	private int N;

	public Deque() {
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return N;
	}

	public void addFirst(Item item) {
		check(item);
		if (isEmpty()) {
			first = new Node(null, item, null);
			last = first;
		} else {
			first.previous = new Node(null, item, first);
			first = first.previous;
		}

		N++;
	}

	private void check(Item item) {
		if (item == null)
			throw new NullPointerException();
	}
	
	private void checkEmpty() {
		if (isEmpty())
			throw new NoSuchElementException();
	}

	public void addLast(Item item) {
		check(item);
		if (isEmpty()) {
			last = new Node(last, item, last);
			first = last;
		} else {
			last.next = new Node(last, item, null);
			last = last.next;
		}
		N++;

	}

	public Item removeFirst() {
		checkEmpty();
		Item item = first.item;
		first = first.next;
		if(first != null) first.previous = null;
		N--;
		if(isEmpty()) {
			last = null;
		}
		return item;
	}

	

	public Item removeLast() {
		checkEmpty();
		Item item = last.item;
		last = last.previous;
		if(last != null) last.next = null;
		N--;
		if(isEmpty()) 
			first = null;
		
		return item;
	}

	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private Node head = first;

			@Override
			public boolean hasNext() {
				return head != null;
			}

			@Override
			public Item next() {
				if (!hasNext())
					throw new NoSuchElementException();
				Item item = head.item;
				head = head.next;
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static void main(String[] args) {
		Deque<Integer> deque = new Deque<Integer>();
		
		deque.addFirst(0);
        deque.addFirst(1);
//        deque.addFirst(2);
//        deque.addFirst(3);
        deque.removeLast();
//        deque.addFirst(5);
//        deque.isEmpty();
//        deque.addFirst(7);
//        deque.addFirst(8);
        deque.removeLast();
		
		for(Integer sttr: deque)
			System.out.println(sttr);
	}

	private class Node {
		Node previous;
		Item item;
		Node next;

		public Node(Node previous, Item item, Node next) {
			this.item = item;
			this.previous = previous;
			this.next = next;
		}
	}
}

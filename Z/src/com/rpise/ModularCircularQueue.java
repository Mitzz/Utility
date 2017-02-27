package com.rpise;

public class ModularCircularQueue {

	private static int first = -1;
	private static int last = -1;
	private static int[] q;
	private static int capacity;

	public static void setCapacity(int capacity) {
		ModularCircularQueue.capacity = capacity;
		q = new int[capacity];
	}

	public static void enqueue(int v) {
		System.out.println(String.format("Before E First Index: %s, Last Index: %s", first, last));
		if (first == (last + 1) % capacity) {
			System.out.println("Full");
			return;
		} else if (isEmpty()) {
			last++;
			first = last;
			q[first] = v;
		} else {
			last = (last + 1) % capacity;
			q[last] = v;

		}
		System.out.println("Enqueue Element: " + v);
		System.out.println(String.format("After E First Index: %s, Last Index: %s", first, last));

	}

	public static boolean isEmpty() {
		return first == -1;
	}

	public static int dequeue() {
		System.out.println(String.format("Before D First Index: %s, Last Index: %s", first, last));
		int temp = -1;
		if (isEmpty()) {
			System.out.println("Empty");
		} else if (first == last) {
			temp = q[first];
			first = -1;
		} else {
			temp = q[first];
			first = (first + 1) % capacity;
		}
		System.out.println(String.format("After D First Index: %s, Last Index: %s", first, last));

		return temp;
	}

	public static void display() {
		if (isEmpty()) {
			System.out.println("Nothing to disply");
			return;
		}
		int current = first;

		while (current != last) {
			System.out.println(q[current]);
			current = (current + 1) % capacity;
		}
		System.out.println(q[current]);
	}
}

class TestModularCircularQueue {
	public static void main(String[] args) {
		ModularCircularQueue.setCapacity(3);
		ModularCircularQueue.enqueue(10);
		System.out.println("Dequeue: " + ModularCircularQueue.dequeue());
		ModularCircularQueue.enqueue(20);
		ModularCircularQueue.enqueue(30);
		ModularCircularQueue.enqueue(40);

		ModularCircularQueue.enqueue(50);
		System.out.println("Dequeue: " + ModularCircularQueue.dequeue());
		ModularCircularQueue.enqueue(60);
		System.out.println("Dequeue: " + ModularCircularQueue.dequeue());

		System.out.println("Dequeue: " + ModularCircularQueue.dequeue());
		System.out.println("Dequeue: " + ModularCircularQueue.dequeue());
		ModularCircularQueue.enqueue(70);

		ModularCircularQueue.display();
	}
}
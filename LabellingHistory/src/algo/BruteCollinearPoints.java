package algo;

import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

public class BruteCollinearPoints {
	private int numberOfSegments;
	private final LineSegment[] segments;

	public BruteCollinearPoints(Point[] points) {
		checkNotNull(points);
		if (isRepeated(points))
			throw new IllegalArgumentException();

		Queue<LineSegment> queue = new Queue<LineSegment>();
		for (int p = 0; p < points.length; p++) {
			Point pointP = points[p];
			for (int q = p + 1; q < points.length; q++) {
				Point pointQ = points[q];
				for (int r = q + 1; r < points.length; r++) {
					Point pointR = points[r];
					for (int s = r + 1; s < points.length; s++) {
						Point pointS = points[s];

						double slopePtoQ = pointP.slopeTo(pointQ);
						double slopePtoR = pointP.slopeTo(pointR);
						double slopePtoS = pointP.slopeTo(pointS);

						if (slopePtoQ == slopePtoR && slopePtoQ == slopePtoS) {
							queue.enqueue(new LineSegment(pointP, pointS));
							numberOfSegments++;
						}
					}
				}
			}
		}

		segments = new LineSegment[queue.size()];
		while (!queue.isEmpty()) {
			LineSegment segment = queue.dequeue();
			segments[queue.size()] = segment;
		}
	}

	private void checkNotNull(Object obj) {
		if (obj == null)
			throw new NullPointerException();
	}

	private void checkNotNull(Point[] points) {
		if (points == null)
			throw new NullPointerException();
		for (int i = 0; i < points.length; i++)
			checkNotNull(points[i]);
	}

	private boolean isRepeated(Comparable[] c) {
		Arrays.sort(c);
		for (int i = 0; i < c.length - 1; i++)
			if (c[i].compareTo(c[i + 1]) == 0)
				return true;
		return false;
	}

	public int numberOfSegments() {
		return numberOfSegments;
	}

	public LineSegment[] segments() {
		return segments;
	}
}

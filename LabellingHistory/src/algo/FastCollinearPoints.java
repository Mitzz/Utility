package algo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

	private int numberOfSegments;
	private LineSegment[] segments;

	public FastCollinearPoints(Point[] points) {
		checkNotNull(points);
		if (isRepeated(points))
			throw new IllegalArgumentException();

		Queue<LineSegment> queue = new Queue<LineSegment>();
		
		Set<Double> slopes = new HashSet<Double>();
		for (int p = 0; p < points.length; p++) {
			Arrays.sort(points);
			Point pointP = points[p];
			Arrays.sort(points, pointP.slopeOrder());
			for (int q = 1; q < points.length;) {
				Point pointQ = points[q];
				double slope = pointP.slopeTo(pointQ);
				int counter = 0;
				for (int r = q + 1; r < points.length; r++) {
					Point pointR = points[r];
					double currentSlope = pointP.slopeTo(pointR);
					if (slope == currentSlope) {
						counter++;
					} else {
						break;
					}
				}
				if (counter > 1 && !slopes.contains(slope)) {
					slopes.add(slope);
					
					queue.enqueue(new LineSegment(pointP, points[counter + q]));
					numberOfSegments++;
				}
				q = q + counter + 1;
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

	private void checkNotNull(Object[] arr) {
		if (arr == null)
			throw new NullPointerException();
		for (int i = 0; i < arr.length; i++)
			checkNotNull(arr[i]);
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

	public static void main(String[] args) {

		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}

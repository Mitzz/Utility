package algo;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private boolean[][] siteStatus;
	private int N;
	private WeightedQuickUnionUF uf;
	private int noOfOpenSites;

	public Percolation(int n) {
		check(n);
		N = n;
		uf = new WeightedQuickUnionUF(N * N + 2);
		siteStatus = new boolean[N][N];
	}

	public void open(int row, int col) {
		check(row, col);
		if (!isOpen(row, col)) {
			siteStatus[row - 1][col - 1] = true;
			noOfOpenSites++;
			openAdjacent(row, col);
		}
	}

	private void openAdjacent(int row, int col) {
		if (row != 1 && isUpOpen(row, col))
			uf.union(transform(row, col), transform(row - 1, col));

		if (row != N && isDownOpen(row, col))
			uf.union(transform(row, col), transform(row + 1, col));

		if (col != 1 && isLeftOpen(row, col))
			uf.union(transform(row, col), transform(row, col - 1));

		if (col != N && isRightOpen(row, col))
			uf.union(transform(row, col), transform(row, col + 1));

		if (row == 1)
			uf.union(0, transform(row, col));
		if (row == N)
			uf.union(N * N + 1, transform(row, col));

	}

	private void check(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
	}

	private void check(int row, int col) {
		int n = row;
		if (n < 1 || n > N)
			throw new IndexOutOfBoundsException();
		n = col;
		if (n < 1 || n > N)
			throw new IndexOutOfBoundsException();

	}

	private boolean isRightOpen(int row, int col) {
		return isOpen(row, col + 1);
	}

	private boolean isLeftOpen(int row, int col) {
		return isOpen(row, col - 1);
	}

	private boolean isDownOpen(int row, int col) {
		return isOpen(row + 1, col);
	}

	private int transform(int row, int col) {
		return ((row - 1) * N) + col;
	}

	private boolean isUpOpen(int row, int col) {
		return isOpen(row - 1, col);
	}

	public boolean isOpen(int row, int col) {
		check(row, col);
		return siteStatus[row - 1][col - 1];
	}

	public boolean isFull(int row, int col) {
		check(row, col);
		return uf.connected(0, transform(row, col));
	}

	public int numberOfOpenSites() {
		return noOfOpenSites;
	}

	public boolean percolates() {
		return uf.connected(0, (N * N) + 1);
	}

	public static void main(String[] args) throws InterruptedException {
		In in = new In(args[0]);
		int N = in.readInt();
		System.out.println(N);
		Percolation p = new Percolation(N);

		while (!p.percolates()) {
			int n1 = in.readInt();
			int n2 = in.readInt();
			System.out.println(String.format("%d: %d", n1, n2));
			p.open(n1, n2);
			System.out.println(p.noOfOpenSites);
		}
		System.out.println(p.noOfOpenSites / (N * N * 1.0));
	}
}

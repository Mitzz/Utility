package algo;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] threshold;

	public PercolationStats(int n, int trials) {
		threshold = new double[trials];
		while (trials > 0) {
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
			}
			threshold[trials - 1] = p.numberOfOpenSites() / (1.0 * n * n);
			trials--;
		}
		
	}

	public double mean() {
		return StdStats.mean(threshold);
	}

	public double stddev() {
		return StdStats.stddev(threshold);
	}

	public double confidenceLo() {
		double stddev = stddev();
		double mean = mean();
		return  mean - ((1.96 * stddev) / Math.sqrt(threshold.length));
	}

	public double confidenceHi() {
		double stddev = stddev();
		double mean = mean();
		return mean + ((1.96 * stddev) / Math.sqrt(threshold.length));
	}

	public static void main(String[] args) {
		PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println(String.format("mean                    = %s", stats.mean()));
		System.out.println(String.format("stddev                  = %s", stats.stddev()));
		System.out.println(String.format("95%% confidence interval = [%s, %s]", stats.confidenceLo(), stats.confidenceHi()));
	}

} 
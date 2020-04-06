/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 3-11-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int trials;
    private double mean;
    private double std;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n<=0 or trials is <=0");
        }

        double[] thresholds = new double[trials];
        this.trials = trials;
        for (int i = 0; i <= trials - 1; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int index = StdRandom.uniform(0, n * n) + 1;
                if (index % n == 0) {
                    p.open(index / n, n);
                }
                else {
                    p.open(index / n + 1, index % n);
                }

            }
            thresholds[i] = (double) p.numberOfOpenSites() / (n * n);
        }
        this.mean = StdStats.mean(thresholds);
        this.std = StdStats.stddev(thresholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.std;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - 1.96 * this.std / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + 1.96 * this.std / Math.sqrt(this.trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        // PercolationStats pstat = new PercolationStats(200, 100);
        // System.out.println(pstat.mean());
        // System.out.println(pstat.stddev());
        // System.out.println("[" + pstat.confidenceLo() + "," + pstat.confidenceHi() + "]");
    }
}

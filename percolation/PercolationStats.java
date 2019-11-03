/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 1 June 12:20
 *  Description: Percolation stats implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int n;
    private int results[];
    /**
     * Perform trials independent experiments on an n-by-n grid
     * @param n - width of square
     * @param trials - number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0 ) {
            throw new IllegalArgumentException("One of argument less or equal than 0");
        }
        this.n = n;
        results = new int[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }
            results[i] = perc.numberOfOpenSites();
        }
    }

    /**
     * Calculate average of fractions of open sites
     * @return average of fractions of open sites
     */
    public double mean() {
        return StdStats.mean(fractions());
    }

    /**
     * Calculate fractions of open sites
     * @return array of fractions
     */
    private double[] fractions() {
        double [] fractions = new double[results.length];
        for(int i = 0; i < results.length; i++) {
            fractions[i] = (double) results[i] / (n * n);
        }
        return fractions;
    }

    /**
     * Calculate standard deviation of fractions
     * @return standard deviation of fractions
     */
    public double stddev() {
        return StdStats.stddev(fractions());
    }

    /**
     * Calculate low bound of confidence
     * @return low bound of confidence
     */
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(results.length);
    }

    /**
     * Calculate high bound of confidence
     * @return high bound of confidence
     */
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(results.length);
    }

    /**
     * Test function
     * @param args - arguments
     */
    public static void main(String[] args) {
        Stopwatch watch = new Stopwatch();
        if (args.length < 2) {
            throw new IllegalArgumentException("Illegal count of arguments");
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        System.out.println("Mean = "+percStats.mean());
        System.out.println("Std dev = "+percStats.stddev());
        System.out.println("Confidence low = "+percStats.confidenceLo());
        System.out.println("Confidence high = "+percStats.confidenceHi());
        System.out.println("Elapsed time = "+watch.elapsedTime());
    }
}

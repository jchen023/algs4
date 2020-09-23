import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final double[] trialdata;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;
        trialdata = new double[trials];
        for (int i = 0; i < trials; i++) {
            trialdata[i] = percstat(n);
        }
    }

    private double percstat(int n) {
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            percolation.open(row, col);
        }
        double stat = percolation.numberOfOpenSites() / (double) (n * n);
        return stat;
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialdata);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialdata);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - stddev() * 1.96 / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + stddev() * 1.96 / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException();
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.println("mean =\t" + percolationStats.mean());
        StdOut.println("stddev =\t" + percolationStats.stddev());
        StdOut.println("95% Confidence Interval =\t[" + percolationStats.confidenceLo() + "," + percolationStats.confidenceHi() + "]");
    }
}

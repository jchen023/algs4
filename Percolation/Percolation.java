import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF wqUF;
    private final WeightedQuickUnionUF backwash_wqUF;
    private boolean[][] siteisopen;
    private int count = 0;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        wqUF = new WeightedQuickUnionUF(n * n + 2);
        backwash_wqUF = new WeightedQuickUnionUF(n * n + 1);
        siteisopen = new boolean[n][n];
    }

    private void checkArg(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) throw new IllegalArgumentException();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkArg(row, col);
        if (!siteisopen[row - 1][col - 1]) {
            siteisopen[row - 1][col - 1] = true;
            count++;
            int wqUFindex = (row - 1) * n + col;
            //connect open site to the site on the left
            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    wqUF.union(wqUFindex, wqUFindex - 1);
                    backwash_wqUF.union(wqUFindex, wqUFindex - 1);
                }
            }
            //connect open site to the site on the right
            if (col < n) {
                if (isOpen(row, col + 1)) {
                    wqUF.union(wqUFindex, wqUFindex + 1);
                    backwash_wqUF.union(wqUFindex, wqUFindex + 1);
                }
            }
            //connect open site to the site above, if first row, connect to the top
            if (row > 1) {
                if (isOpen(row - 1, col)) {
                    wqUF.union(wqUFindex, wqUFindex - n);
                    backwash_wqUF.union(wqUFindex, wqUFindex - n);
                }
            } else {
                wqUF.union(wqUFindex, 0);
                backwash_wqUF.union(wqUFindex, 0);
            }
            //connect open site to the site under, if last row, connect to the bottom
            if (row < n) {
                if (isOpen(row + 1, col)) {
                    wqUF.union(wqUFindex, wqUFindex + n);
                    backwash_wqUF.union(wqUFindex, wqUFindex + n);
                }
            } else {
                wqUF.union(wqUFindex, n * n + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkArg(row, col);
        return siteisopen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkArg(row, col);
        int wqUFindex = (row - 1) * n + col;
        return backwash_wqUF.find(wqUFindex) == backwash_wqUF.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return wqUF.find(0) == wqUF.find(n * n + 1);
    }


    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            percolation.open(row, col);
        }
        double stat = percolation.numberOfOpenSites() / (double) (n * n);
        StdOut.print(stat);
    }
}

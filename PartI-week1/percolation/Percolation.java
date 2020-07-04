/* *****************************************************************************
 *  Name: Percolation
 *  Date: 3-11-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    private boolean[][] openstate;
    private int opencount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is <=0");
        }

        this.n = n;
        top = n * n;
        bottom = n * n + 1;
        uf1 = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        openstate = new boolean[n][n];
        for (int i = 0; i <= n - 1; i++) {
            for (int j = 0; j <= n - 1; j++) {
                openstate[i][j] = false;
            }
        }
        opencount = 0;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            openstate[row - 1][col - 1] = true;
            opencount++;
            int index = (row - 1) * n + col - 1;

            if (row == 1) {
                uf1.union(index, top);
                uf2.union(index, top);
            }

            if (row == this.n) {
                uf1.union(index, bottom);
            }

            if (isOpen2(row - 1, col)) {
                uf1.union(index, (row - 2) * n + col - 1);
                uf2.union(index, (row - 2) * n + col - 1);
            }

            if (isOpen2(row + 1, col)) {
                uf1.union(index, row * n + col - 1);
                uf2.union(index, row * n + col - 1);
            }

            if (isOpen2(row, col - 1)) {
                uf1.union(index, (row - 1) * n + col - 2);
                uf2.union(index, (row - 1) * n + col - 2);
            }

            if (isOpen2(row, col + 1)) {
                uf1.union(index, (row - 1) * n + col);
                uf2.union(index, (row - 1) * n + col);
            }

        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("row/col out of range");
        }
        return openstate[row - 1][col - 1];
    }

    // are the sites next to (row, col) exist&open?
    private boolean isOpen2(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            return false;
        }
        else {
            return openstate[row - 1][col - 1];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("row/col out of range");
        }
        return uf2.connected((row - 1) * n + col - 1, top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.opencount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf1.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Percolation p = new Percolation(1);
        // p.open(1, 1);
        // System.out.print(p.percolates());
    }
}

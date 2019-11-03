import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name: Percolation
 *  Date: 31 May 19:50
 *  Description: Percolation implemetation
 **************************************************************************** */

/**
 * Percolation class
 */
public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean[] openSites;
    private int n;
    private final int topRoot;
    private final int bottomRoot;

    /**
     * Check whether site is open?
     * @param i - index of site
     * @return bool - site is open?
     */
    private boolean checkOpen(int i) {
        return this.openSites[i];
    }

    /**
     * Calc index
     * @param row - row of site (1 <= row <= n)
     * @param col - column of site ( 1 <= col <= n)
     * @throws IllegalArgumentException
     * @return index
     */
    private int calcIndex(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        return (row - 1) * n + col - 1;
    }

    /**
     * Calculate connectable sites. Sites need to be exist, open and unique
     * @param i - index of site
     * @return - array of indexes of connectable sites
     */
    private int[] calcConnectableSites(int i) {
        int checkedSites[];
        boolean connectToTop = false;
        boolean connectToBottom = false;
        int fixedSites[] = {-1, -1, -1, -1};
        int l = 0;
        if (i % n == 0) {
            checkedSites = new int[3];
            checkedSites[0] = i-n;
            checkedSites[1] = i+1;
            checkedSites[2] = i+n;
        } else if (i % n == n - 1) {
            checkedSites = new int[3];
            checkedSites[0] = i-1;
            checkedSites[1] = i-n;
            checkedSites[2] = i+n;
        } else {
            checkedSites = new int[4];
            checkedSites[0] = i-1;
            checkedSites[1] = i-n;
            checkedSites[2] = i+1;
            checkedSites[3] = i+n;
        }
        for (int k = 0; k < checkedSites.length; k++) {
            if (checkedSites[k] < 0 && !connectToTop) {
                fixedSites[l] = topRoot;
                ++l;
                connectToTop = true;
            } else if (checkedSites[k] >= topRoot && !connectToBottom) {
                fixedSites[l] = bottomRoot;
                ++l;
                connectToBottom = true;
            } else if (checkedSites[k] >= 0 && checkedSites[k] < topRoot && checkOpen(checkedSites[k])) {
                fixedSites[l] = checkedSites[k];
                ++l;
            }
        }
        return fixedSites;
    }

    /**
     * Constructor
     * @param n - width of square
     */
    public Percolation(int n) {
        if (n <= 0 ) {
            throw new IllegalArgumentException("Argument less or equal than 0");
        }
        this.n = n;
        int size = n * n + 2; // +2 cause we need two roots in top and bottom
        topRoot = size - 2;
        bottomRoot = size - 1;
        uf = new WeightedQuickUnionUF(size);
        openSites = new boolean[size];
        for (int i = 0; i < size; i++) {
            openSites[i] = i == topRoot || i == bottomRoot;
        }
    }

    /**
     * Open site
     * @param row - row of site
     * @param col - col of site
     */
    public void open(int row, int col) {
        int i = calcIndex(row, col);
        openSites[i] = true;

        int connectableSites[] = calcConnectableSites(i);
        for (int site: connectableSites) {
            if(site == -1) {
                continue;
            }
            //System.out.println("Site "+i+". Connect with site "+site);
            uf.union(i, site);
        }
    }

    /**
     * Check whether site is open
     * @param row - row of site
     * @param col - col of site
     * @return bool
     */
    public boolean isOpen(int row, int col) {
        return checkOpen(calcIndex(row, col));
    }

    /**
     * Check whether site is full (open and connected to top root)
     * @param row - row of site
     * @param col - col of site
     * @return bool
     */
    public boolean isFull(int row, int col) {
        int p = calcIndex(row, col);
        return uf.connected(p, topRoot);
    }

    /**
     * Get number of open sites
     * @return number
     */
    public int numberOfOpenSites() {
        int acc = 0;
        for (int i = 0; i < topRoot; i++) {
            if (openSites[i]) {
               ++acc;
            }
        }
        return acc;
    }

    /**
     * Check percolation
     * @return bool
     */
    public boolean percolates() {
        return uf.connected(bottomRoot, topRoot);
    }
}

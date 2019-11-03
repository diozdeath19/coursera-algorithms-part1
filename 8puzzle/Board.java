/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import java.util.ArrayList;


public class Board {
    private int [] tiles;
    private int dimension;
    private int manhattan;
    private int hamming;
    private int blank;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length < 2 || tiles.length > 128) {
            throw new IllegalArgumentException("n is illegal");
        }
        this.tiles = new int[tiles.length * tiles.length];
        this.dimension = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int k = 0; k < tiles[i].length; k++) {
                int offset = i * this.dimension + k;
                this.tiles[offset] = tiles[i][k];
                if (tiles[i][k] == 0) {
                    this.blank = offset;
                }
            }
        }

        this.calcHammingAndManhattan();
    }

    private Board(int[] tiles, int dimension, int blank) {
        this.tiles = tiles;
        this.dimension = dimension;
        this.blank = blank;
    }

    /**
     * @return Board dimension
     */
    public int dimension() {
        return this.dimension;
    }

    /**
     * @return number of tiles out of place
     */
    public int hamming() {
        return this.hamming;
    }

    /**
     * calc hamming and manhattan
     */
    private void calcHammingAndManhattan() {
        int countHamming = 0;
        int countManhattan = 0;
        int i;

        for (i = 0; i < this.tiles.length - 1; i++) {
            if (this.tiles[i] != i + 1) {
                countHamming++;
                if (this.tiles[i] == 0) {
                    continue;
                }
                countManhattan += this.calcManhattanDistance(this.tiles[i] - 1, i);
            }
        }
        if (this.tiles[i] != 0) {
            countManhattan += this.calcManhattanDistance(this.tiles[i] - 1, i);
        }
        this.hamming =  countHamming;
        this.manhattan = countManhattan;
    }

    private int calcManhattanDistance(int goalIndex, int currentIndex) {
        int goalRow = goalIndex / this.dimension;
        int goalCol = goalIndex % this.dimension;
        int curRow = currentIndex / this.dimension;
        int curCol = currentIndex % this.dimension;

        return Math.abs(goalRow  - curRow) + Math.abs(goalCol - curCol);
    }

    /**
     * @return sum of Manhattan distances between tiles and goal
     */
    public int manhattan() {
        return this.manhattan;
    }

    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board board = (Board) y;

        if (this.toString().equals(board.toString())) {
            return true;
        }

        return false;
    }

    /**
     * @return string representation
     */
    public String toString() {
        String output = String.format("%s\n ", this.dimension);
        for (int i = 0; i < this.tiles.length; i++) {
            output += String.format("%s", this.tiles[i]);
            if ((i + 1) % this.dimension != 0) {
                output += " ";
            } else {
                output += " \n ";
            }
        }
        return output;
    }

    /**
     * @return neighbor boards
     */
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborsList = new ArrayList<Board>();

        int leftNeighbor = this.blank - 1;
        // Check if exists and is on the same row
        if (leftNeighbor >= 0 && (leftNeighbor / this.dimension) == (this.blank / this.dimension)) {
            neighborsList.add(this.cloneBoardWithExchange(blank, leftNeighbor));
        }

        int rightNeighbor = this.blank + 1;
        // Check if exists and is on the same row
        if (rightNeighbor < this.tiles.length && (rightNeighbor / this.dimension) == (this.blank / this.dimension)) {
            neighborsList.add(this.cloneBoardWithExchange(this.blank, rightNeighbor));
        }

        int upperNeighbor = this.blank - this.dimension;
        // Check if exists
        if (upperNeighbor >= 0) {
            neighborsList.add(this.cloneBoardWithExchange(this.blank, upperNeighbor));
        }

        int bottomNeighbor = this.blank + this.dimension;
        // Check if exists
        if (bottomNeighbor < this.tiles.length) {
            neighborsList.add(this.cloneBoardWithExchange(this.blank, bottomNeighbor));
        }

        return neighborsList;
    }

    public Board twin() {
        for (int i = 0; i < this.tiles.length - 1; i++) {
            if (this.tiles[i] != 0 && this.tiles[i + 1] != 0 && (i + 1) % this.dimension != 0) {
                return this.cloneBoardWithExchange(i, i+1);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int [][] tiles = {{1, 2, 3}, {4, 0, 8}, {7, 6, 5}};
        Board board = new Board(tiles);
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board.toString());
        for (Board neighbor: board.neighbors()) {
            System.out.println(neighbor.toString());
            System.out.println();
        }
    }

    private Board cloneBoardWithExchange(int i, int k) {
        int[] clonedTiles = new int[this.tiles.length];
        int blank = 0;
        for (int j = 0; j < this.tiles.length; j++) {
            clonedTiles[j] = this.tiles[j];
            if (this.tiles[j] == 0) {
                blank = j;
            }
        }
        Board clone = new Board(clonedTiles, this.dimension, blank);
        clone.exchange(i, k);
        return clone;
    }

    private void exchange(int i, int k) {
        int buf = this.tiles[i];
        this.tiles[i] = this.tiles[k];
        this.tiles[k] = buf;

        if (this.tiles[i] == 0) {
            this.blank = i;
        }

        if (this.tiles[k] == 0) {
            this.blank = k;
        }

        this.calcHammingAndManhattan();
    }
}

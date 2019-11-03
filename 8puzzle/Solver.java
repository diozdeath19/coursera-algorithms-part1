/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<SearchNode> pq;
    private int minMoves;
    private Stack<Board> solution;
    private boolean solvable;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("board is null");
        }
        this.pq = new MinPQ<>();
        this.minMoves = 0;
        this.solution = new Stack<>();
        this.solvable = true;

        pq.insert(new SearchNode(initial, 0, null));
        pq.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode searchNode = this.pq.delMin();
        while (!searchNode.getBoard().isGoal()) {
            process(searchNode);
            searchNode = this.pq.delMin();
        }
        this.minMoves = searchNode.getMoves();

        while (searchNode.getPreviousSearchNode() != null) {
            this.solution.push(searchNode.getBoard());
            searchNode = searchNode.getPreviousSearchNode();
        }
        this.solution.push(searchNode.getBoard());
        if (!searchNode.getBoard().equals(initial)) {
            this.solvable = false;
            this.minMoves = -1;
            this.solution = null;
        }
    }

    private void process(SearchNode searchNode) {
        Board processedBoard = searchNode.getBoard();
        SearchNode previousSearchNode = searchNode.getPreviousSearchNode();
        for (Board neighbor: processedBoard.neighbors()) {
            if (previousSearchNode != null && neighbor.equals(previousSearchNode.getBoard())) {
                continue;
            }
            this.pq.insert(new SearchNode(neighbor, searchNode.getMoves() + 1, searchNode));
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousSearchNode;

        public SearchNode(Board board, int moves, SearchNode previousSearchNode) {
            this.board = board;
            this.moves = moves;
            this.previousSearchNode = previousSearchNode;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPreviousSearchNode() {
            return this.previousSearchNode;
        }

        public int priority() {
            return this.getBoard().manhattan() + this.getMoves();
        }

        public int compareTo(SearchNode searchNode) {
            return Integer.compare(this.priority(), searchNode.priority());
        }
    }

    // // is the initial board solvable? (see below)
    // public boolean isSolvable()
    //
    // min number of moves to solve initial board
    public int moves() {
        return this.minMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return this.solution;
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public static void main(String[] args) {
        int [][] tiles = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        // int [][] tiles = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board = new Board(tiles);
        Solver solver = new Solver(board);
        System.out.println(solver.moves());
        System.out.println(solver.isSolvable());
        for (Board boardStep: solver.solution()) {
            System.out.println(boardStep.toString());
            System.out.println();
        }
    }
}

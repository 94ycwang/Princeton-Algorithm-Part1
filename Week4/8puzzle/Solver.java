/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Node currentnode = null;
    private boolean issolve = true;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null input");

        MinPQ<Node> minpq = new MinPQ<>();
        MinPQ<Node> minpqtwin = new MinPQ<>();

        Node node = new Node(initial, 0, null);
        Node nodetwin = new Node(initial.twin(), 0, null);

        minpq.insert(node);
        minpqtwin.insert(nodetwin);
        while (true) {
            currentnode = minpq.delMin();
            Node currenttwinnode = minpqtwin.delMin();

            if (currentnode.board.isGoal()) break;
            if (currenttwinnode.board.isGoal()) {
                issolve = false;
                break;
            }

            Iterable<Board> neighbors = currentnode.board.neighbors();
            for (Board newboard : neighbors) {
                if (currentnode.parentnode == null) {
                    minpq.insert(new Node(newboard, currentnode.move + 1, currentnode));

                }
                else if (!newboard.equals(currentnode.parentnode.board)) {
                    minpq.insert(new Node(newboard, currentnode.move + 1, currentnode));

                }
            }

            Iterable<Board> neighborstwin = currenttwinnode.board.neighbors();
            for (Board newboard : neighborstwin) {
                if (currenttwinnode.parentnode == null) {
                    minpqtwin.insert(new Node(newboard, currenttwinnode.move + 1, currenttwinnode));
                }
                else if (!newboard.equals(currenttwinnode.parentnode.board)) {
                    minpqtwin.insert(new Node(newboard, currenttwinnode.move + 1, currenttwinnode));

                }
            }

        }

    }

    private class Node implements Comparable<Node> {
        private Board board;
        private int move;
        private Node parentnode;
        private int priority;

        Node(Board board, int move, Node parentnode) {
            this.board = board;
            this.move = move;
            this.parentnode = parentnode;
            this.priority = board.manhattan() + move;
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return issolve;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return currentnode.move;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> stack = new Stack<>();
        Node nodecopy = currentnode;
        while (nodecopy != null) {
            stack.push(nodecopy.board);
            nodecopy = nodecopy.parentnode;
        }
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}

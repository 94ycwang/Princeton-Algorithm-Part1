/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int n;
    private final int[][] board;
    private int hammingdist = -1;
    private int manhattandist = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = tiles.clone();
    }

    // string representation of this board
    public String toString() {
        String boardstring = new String(String.valueOf(n) + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardstring += " " + board[i][j] + " ";
            }
            boardstring += "\n";
        }
        return boardstring;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingdist != -1) return hammingdist;
        hammingdist = 0;
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != k + 1) hammingdist++;
                k++;
            }
        }
        return hammingdist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattandist != -1) return manhattandist;
        manhattandist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int row = (board[i][j] - 1) / n;
                int col = (board[i][j] - 1) % n;
                if (board[i][j] != 0) {
                    manhattandist += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return manhattandist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x0 = 0;
        int y0 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    x0 = i;
                    y0 = j;
                }
            }
        }

        Stack<Board> stack = new Stack<>();

        if (x0 - 1 > -1) {
            stack.push(new Board(exch(x0, y0, x0 - 1, y0)));
        }

        if (x0 + 1 < n) {
            stack.push(new Board(exch(x0, y0, x0 + 1, y0)));
        }

        if (y0 - 1 > -1) {
            stack.push(new Board(exch(x0, y0, x0, y0 - 1)));
        }

        if (y0 + 1 < n) {
            stack.push(new Board(exch(x0, y0, x0, y0 + 1)));
        }

        return stack;
    }

    private int[][] exch(int x1, int y1, int x2, int y2) {
        int[][] newboard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        int temp = newboard[x1][y1];
        newboard[x1][y1] = newboard[x2][y2];
        newboard[x2][y2] = temp;
        return newboard;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (board[0][0] != 0 && board[0][1] != 0) {
            return new Board(exch(0, 0, 0, 1));
        }
        else {
            return new Board(exch(1, 0, 1, 1));
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] test = new int[3][3];
        // test[0][0] = 1;
        // test[0][1] = 2;
        // test[0][2] = 3;
        // test[1][0] = 4;
        // test[1][1] = 5;
        // test[1][2] = 6;
        // test[2][0] = 7;
        // test[2][1] = 8;
        // test[2][2] = 0;
        // Board testboard = new Board(test);
        // System.out.println(testboard.toString());
        // System.out.println(testboard.hamming());
        // test[2][2] = 9;
        // System.out.println(testboard.toString());
        // Iterable<Board> testneighbors = testboard.neighbors();
        // testneighbors.forEach((neighbor) -> System.out.println(neighbor.toString()));
    }

}

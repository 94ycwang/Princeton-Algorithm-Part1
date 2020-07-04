/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private SET<String> validWords;
    private Node root;

    private static class Node {
        private boolean isEnd = false;
        private Node[] next = new Node[26];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException("");
        root = new Node();
        for (String s : dictionary) {
            Node node = root;
            for (int i = 0; i < s.length(); i++) {
                int ind = s.charAt(i) - (int) 'A';
                if (node.next[ind] == null)
                    node.next[ind] = new Node();
                node = node.next[ind];
                if (i == s.length() - 1)
                    node.isEnd = true;
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        validWords = new SET<>();
        int col = board.cols(), row = board.rows();
        boolean[][] visit = new boolean[row][col];
        char[][] charBoard = new char[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                charBoard[i][j] = board.getLetter(i, j);
            }

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                dfs(root, "", i, j, charBoard, visit);
            }

        return validWords;
    }

    private void dfs(Node node, String prefix, int i, int j, char[][] board, boolean[][] visit) {
        if (i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visit[i][j]) {
            visit[i][j] = true;
            int ind = board[i][j] - 'A';
            if (node.next[ind] != null) {
                prefix += board[i][j];
                node = node.next[ind];

                if (board[i][j] == 'Q' && node.next['U' - 'A'] == null) {
                    visit[i][j] = false;
                    return;
                }
                if (board[i][j] == 'Q' && node.next['U' - 'A'] != null) {
                    prefix += 'U';
                    node = node.next['U' - 'A'];
                }
                if (node.isEnd && prefix.length() > 2)
                    validWords.add(prefix);
                int[] di = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
                int[] dj = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };
                for (int k = 0; k < di.length; k++)
                    dfs(node, prefix, i + di[k], j + dj[k], board, visit);

            }
            visit[i][j] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("");
        if (!contains(word)) return 0;
        if (word.length() < 3) return 0;
        else if (word.length() < 5) return 1;
        else if (word.length() == 5) return 2;
        else if (word.length() == 6) return 3;
        else if (word.length() == 7) return 5;
        else return 11;

    }

    private boolean contains(String s) {
        Node node = root;
        for (int i = 0; i < s.length(); i++) {
            if (node.next[s.charAt(i) - 'A'] != null)
                node = node.next[s.charAt(i) - 'A'];
            else break;

            if (i == s.length() - 1 && node.isEnd) return true;
        }
        return false;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

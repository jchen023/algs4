import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private char[][] c;
    private boolean[][] visited;
    private int row;
    private int col;
    private Node root;

    private static final int R = 26;

    // R-way trie node
    private static class Node {
        private boolean hasW;
        private Node[] next = new Node[R];
    }

    private boolean get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.hasW;
    }


    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d + 1);
    }


    private void put(String key, boolean hasW) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        else root = put(root, key, hasW, 0);
    }

    private Node put(Node x, String key, boolean hasW, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.hasW = hasW;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 'A'] = put(x.next[c - 'A'], key, hasW, d + 1);
        return x;
    }


    private boolean hasPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        if (x == null) return false;
        return true;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String w : dictionary) {
            put(w, true);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        row = board.rows();
        col = board.cols();
        c = new char[row][col];
        visited = new boolean[row][col];
        HashSet<String> words = new HashSet<String>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                c[i][j] = board.getLetter(i, j);
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                searchForWords(i, j, "", words);
            }
        }
        return words;
    }


    private void searchForWords(int i, int j, String s, HashSet<String> words) {
        visited[i][j] = true;
        if (c[i][j] == 'Q') s = s + "QU";
        else s = s + c[i][j];
        if (hasPrefix(s)) {
            if (s.length() >= 3 && get(s)) words.add(s);
            for (int k = Math.max(0, i - 1); k <= Math.min(row - 1, i + 1); k++) {
                for (int l = Math.max(0, j - 1); l <= Math.min(col - 1, j + 1); l++) {
                    if (!visited[k][l]) {
                        searchForWords(k, l, s, words);
                    }
                }
            }
        }

        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new NullPointerException();
        if (!get(word)) return 0;
        if (word.length() <= 2) return 0;
        else if (word.length() <= 4) return 1;
        else if (word.length() == 5) return 2;
        else if (word.length() == 6) return 3;
        else if (word.length() == 7) return 5;
        else return 11;
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

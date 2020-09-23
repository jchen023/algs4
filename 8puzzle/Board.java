import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String s = size + "\n ";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s = s + tiles[i][j] + "  ";
            }
            s = s + "\n ";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != size * i + j + 1 && tiles[i][j] != 0) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int row = (tiles[i][j] - 1) / size;
                int col = (tiles[i][j] - 1) % size;
                if (tiles[i][j] != 0) manhattan += Math.abs(row - i) + Math.abs(col - j);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.size != this.size) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int[][] tilescopy1 = new int[size][size];
        int[][] tilescopy2 = new int[size][size];
        int[][] tilescopy3 = new int[size][size];
        int[][] tilescopy4 = new int[size][size];
        int row = 0;
        int col = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
                tilescopy1[i][j] = tiles[i][j];
                tilescopy2[i][j] = tiles[i][j];
                tilescopy3[i][j] = tiles[i][j];
                tilescopy4[i][j] = tiles[i][j];
            }
        }
        if (row < size - 1) {
            tilescopy1[row][col] = tilescopy1[row + 1][col];
            tilescopy1[row + 1][col] = 0;
            neighbors.add(new Board(tilescopy1));
        }
        if (row > 0) {
            tilescopy2[row][col] = tilescopy2[row - 1][col];
            tilescopy2[row - 1][col] = 0;
            neighbors.add(new Board(tilescopy2));
        }
        if (col < size - 1) {
            tilescopy3[row][col] = tilescopy3[row][col + 1];
            tilescopy3[row][col + 1] = 0;
            neighbors.add(new Board(tilescopy3));
        }
        if (col > 0) {
            tilescopy4[row][col] = tilescopy4[row][col - 1];
            tilescopy4[row][col - 1] = 0;
            neighbors.add(new Board(tilescopy4));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twintiles = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                twintiles[i][j] = tiles[i][j];
            }
        }

        if (twintiles[0][0] * twintiles[0][1] == 0) {
            int temp = twintiles[1][0];
            twintiles[1][0] = twintiles[1][1];
            twintiles[1][1] = temp;
        }
        else {
            int temp = twintiles[0][0];
            twintiles[0][0] = twintiles[0][1];
            twintiles[0][1] = temp;
        }
        return new Board(twintiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.neighbors().toString());
        StdOut.println(initial.twin().toString());
    }

}

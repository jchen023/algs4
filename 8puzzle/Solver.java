import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private solverNode poppedNode;


    private class solverNode implements Comparable<solverNode> {
        private final Board currentBoard;
        private final solverNode previousNode;
        private final int step;
        private final int priority;

        private solverNode(Board currentBoard) {
            if (currentBoard == null) throw new IllegalArgumentException();
            this.currentBoard = currentBoard;
            this.step = 0;
            this.previousNode = null;
            priority = currentBoard.manhattan();
        }

        private solverNode(Board currentBoard, int step, solverNode previousNode) {
            if (currentBoard == null) throw new IllegalArgumentException();
            this.currentBoard = currentBoard;
            this.step = step;
            this.previousNode = previousNode;
            priority = currentBoard.manhattan() + step;
        }

        public int compareTo(solverNode that) {
            if (that == null) throw new IllegalArgumentException();
            int priorityA = this.priority;
            int priorityB = that.priority;
            return Integer.compare(priorityA, priorityB);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<solverNode> pq = new MinPQ<>(2);
        pq.insert(new solverNode(initial));
        MinPQ<solverNode> pqtwin = new MinPQ<>(2);
        solverNode poppedNodetwin;
        Board twin = initial.twin();
        pqtwin.insert(new solverNode(twin));
        Board prevBoard;
        Board prevBoardtwin;
        do {
            poppedNode = pq.delMin();
            poppedNodetwin = pqtwin.delMin();
            if (poppedNode.previousNode != null)
                prevBoard = poppedNode.previousNode.currentBoard;
            else prevBoard = null;
            if (poppedNodetwin.previousNode != null)
                prevBoardtwin = poppedNodetwin.previousNode.currentBoard;
            else prevBoardtwin = null;

            for (Board s : poppedNode.currentBoard.neighbors()) {
                if (!s.equals(prevBoard))
                    pq.insert(new solverNode(s, poppedNode.step + 1, poppedNode));
            }
            for (Board s : poppedNodetwin.currentBoard.neighbors()) {
                if (!s.equals(prevBoardtwin))
                    pqtwin.insert(new solverNode(s, poppedNodetwin.step + 1, poppedNodetwin));
            }
        } while (!poppedNode.currentBoard.isGoal() && !poppedNodetwin.currentBoard.isGoal());
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return poppedNode.currentBoard.isGoal();
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return poppedNode.step;
        }
        else return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> solution = new Stack<Board>();
        solverNode node = poppedNode;
        do {
            solution.push(node.currentBoard);
            node = node.previousNode;

        } while (node != null);
        if (isSolvable()) return solution;
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board.toString());
        }
    }

}

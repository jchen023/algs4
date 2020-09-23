import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        BreadthFirstDirectedPaths b1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b2 = new BreadthFirstDirectedPaths(G, w);
        int dist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (b1.hasPathTo(i) && b2.hasPathTo(i)) {
                int y = b1.distTo(i) + b2.distTo(i);
                if (y < dist) {
                    dist = y;
                }
            }
        }
        if (dist == Integer.MAX_VALUE) return -1;
        return dist;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths b1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b2 = new BreadthFirstDirectedPaths(G, w);
        int dist = Integer.MAX_VALUE;
        int id = 0;
        for (int i = 0; i < G.V(); i++) {
            if (b1.hasPathTo(i) && b2.hasPathTo(i)) {
                int y = b1.distTo(i) + b2.distTo(i);
                if (y < dist) {
                    dist = y;
                    id = i;
                }
            }
        }
        if (dist == Integer.MAX_VALUE) return -1;
        return id;
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths b1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b2 = new BreadthFirstDirectedPaths(G, w);
        int dist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (b1.hasPathTo(i) && b2.hasPathTo(i)) {
                int y = b1.distTo(i) + b2.distTo(i);
                if (y < dist) {
                    dist = y;
                }
            }
        }
        if (dist == Integer.MAX_VALUE) return -1;
        return dist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths b1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b2 = new BreadthFirstDirectedPaths(G, w);
        int dist = Integer.MAX_VALUE;
        int id = 0;
        for (int i = 0; i < G.V(); i++) {
            if (b1.hasPathTo(i) && b2.hasPathTo(i)) {
                int y = b1.distTo(i) + b2.distTo(i);
                if (y < dist) {
                    dist = y;
                    id = i;
                }
            }
        }
        if (dist == Integer.MAX_VALUE) return -1;
        return id;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

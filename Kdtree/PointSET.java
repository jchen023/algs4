import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> PointSet;

    // construct an empty set of points
    public PointSET() {
        PointSet = new TreeSet<Point2D>();
    }


    // is the set empty?
    public boolean isEmpty() {
        return PointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return PointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        PointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return PointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : PointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        LinkedList<Point2D> inrange = new LinkedList<Point2D>();
        for (Point2D p : PointSet) {
            if (rect.contains(p)) inrange.add(p);
        }
        return inrange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (PointSet.isEmpty()) return null;
        MinPQ<Point2D> pq = new MinPQ<Point2D>(p.distanceToOrder());
        for (Point2D points : PointSet) {
            pq.insert(points);
        }
        return pq.min();
    }


    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}

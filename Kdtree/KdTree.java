import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {
    private Node root;

    private LinkedList<Point2D> pointsInRect;
    private Point2D closestPoint;
    private double distance;

    private class Node {
        private double key;
        private final Point2D point;
        private final boolean isX;
        private Node left, right;
        private int size;
        private final RectHV rectangle;

        private Node(Point2D point, boolean isX, double xmin, double ymin, double xmax,
                     double ymax) {
            this.point = point;
            this.isX = isX;
            if (isX) key = point.x();
            else key = point.y();
            rectangle = new RectHV(xmin, ymin, xmax, ymax);
            size = 1;
        }

    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    private int size(Node x) {
        if (x != null) return x.size;
        else return 0;
    }


    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) root = insert(root, p, true, 0, 0, 1, 1);

    }

    private Node insert(Node h, Point2D p, boolean isX, double xmin, double ymin, double xmax,
                        double ymax) {
        if (h == null) return new Node(p, isX, xmin, ymin, xmax, ymax);

        int cmp;
        if (isX) cmp = Double.compare(p.x(), h.key);
        else cmp = Double.compare(p.y(), h.key);

        if (cmp <= 0) {
            if (h.isX)
                h.left = insert(h.left, p, !h.isX, xmin, ymin, h.point.x(), ymax);
            else h.left = insert(h.left, p, !h.isX, xmin, ymin, xmax, h.point.y());

        }
        else {
            if (h.isX)
                h.right = insert(h.right, p, !h.isX, h.point.x(), ymin, xmax, ymax);
            else h.right = insert(h.right, p, !h.isX, xmin, h.point.y(), xmax, ymax);
        }

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        if (p.equals(x.point)) return true;
        boolean isX = x.isX;
        int cmp;
        if (isX) cmp = Double.compare(p.x(), x.key);
        else cmp = Double.compare(p.y(), x.key);


        if (cmp <= 0) return contains(x.left, p);
        else return contains(x.right, p);
    }

    // draw all points to standard draw
    public void draw() {
        Node x = root;
        draw(x);
    }

    private void draw(Node x) {
        if (x != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(x.point.x(), x.point.y());
            if (x.isX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.point.x(), x.rectangle.ymin(), x.point.x(), x.rectangle.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.rectangle.xmin(), x.point.y(), x.rectangle.xmax(), x.point.y());
            }
            draw(x.left);
            draw(x.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        pointsInRect = new LinkedList<Point2D>();
        Node x = root;
        range(x, rect);
        return pointsInRect;
    }

    private void range(Node x, RectHV rect) {
        if (rect.contains(x.point)) pointsInRect.add(x.point);
        if (x.left != null) if (rect.intersects(x.left.rectangle)) range(x.left, rect);
        if (x.right != null) if (rect.intersects(x.right.rectangle)) range(x.right, rect);
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Node x = root;
        closestPoint = root.point;
        distance = p.distanceSquaredTo(root.point);
        nearest(x, p);
        return closestPoint;
    }

    private void nearest(Node x, Point2D p) {
        if (distance >= x.rectangle.distanceSquaredTo(p)) {
            if (distance > x.point.distanceSquaredTo(p)) {
                closestPoint = x.point;
                distance = p.distanceSquaredTo(x.point);
            }
            if (x.left != null && x.right != null) {
                if (x.left.rectangle.distanceSquaredTo(p) < x.right.rectangle
                        .distanceSquaredTo(p)) {
                    nearest(x.left, p);
                    nearest(x.right, p);
                }
                else {
                    nearest(x.right, p);
                    nearest(x.left, p);
                }
            }
            else if (x.left == null && x.right != null) nearest(x.right, p);
            else if (x.left != null) nearest(x.left, p);
        }

    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}

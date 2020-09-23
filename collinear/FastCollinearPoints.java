import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private final ArrayList<Double> includedSlopes = new ArrayList<>();
    private final ArrayList<Point> includedPoints = new ArrayList<>();
    private final ArrayList<Point> pointOrder = new ArrayList<>();


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
        }
        Point[] pointsDefaultSort = Arrays.copyOf(points, points.length);
        int n = 0;
        while (points.length >= 4) {
            Arrays.sort(points, pointsDefaultSort[n].slopeOrder());
            double counterSlope = 0;
            int counter = 0;
            for (int j = 1; j < points.length; j++) {
                double currentSlope = pointsDefaultSort[n].slopeTo(points[j]);
                if (currentSlope == counterSlope) {
                    counter++;
                    if (j == points.length - 1 && counter >= 3) {
                        for (int k = j; k > j - counter; k--) {
                            pointOrder.add(points[k]);
                        }
                        Collections.sort(pointOrder, Collections.reverseOrder());
                        if (distinctLine(counterSlope, pointOrder.get(0))) {
                            lineSegments.add(new LineSegment(points[0], pointOrder.get(0)));
                            includedSlopes.add(counterSlope);
                            includedPoints.add(pointOrder.get(0));
                        }
                        pointOrder.clear();
                    }
                }
                else if (currentSlope != counterSlope && counter < 3) {
                    counter = 1;
                    counterSlope = currentSlope;
                }
                else if (currentSlope != counterSlope && counter >= 3) {
                    for (int k = j - 1; k > j - counter - 1; k--) {
                        pointOrder.add(points[k]);
                    }
                    Collections.sort(pointOrder, Collections.reverseOrder());
                    if (distinctLine(counterSlope, pointOrder.get(0))) {
                        lineSegments.add(new LineSegment(points[0], pointOrder.get(0)));
                        includedSlopes.add(counterSlope);
                        includedPoints.add(pointOrder.get(0));
                    }
                    pointOrder.clear();
                    counter = 1;
                    counterSlope = currentSlope;
                }
            }
            points = Arrays.copyOfRange(points, 1, points.length);

            n++;
        }
    }

    private boolean distinctLine(double slope, Point endpoint) {
        for (int i = 0; i < includedSlopes.size(); i++) {
            if (slope == includedSlopes.get(i) && endpoint == includedPoints.get(i))
                return false;
        }
        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

}

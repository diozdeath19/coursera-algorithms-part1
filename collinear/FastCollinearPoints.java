/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class FastCollinearPoints {
    private static final int COLLINEAR_POINTS_COUNT_FOR_SEGMENT = 3;

    private Point[] points;
    private LinkedList<LineSegment> lineSegments;

    /**
     * @param points - array of points
     * @throws IllegalArgumentException - if one of points is null or repeated
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("point is null");
        }

        for (Point point: points) {
            if (point == null) {
                throw new IllegalArgumentException("point is null");
            }
        }

        Point[] clonedPoints = points.clone();
        Arrays.sort(clonedPoints);
        int i = 0;
        this.points = new Point[clonedPoints.length];
        for (Point point: clonedPoints) {
            int head = Math.max(i - 1, 0);
            if (this.points[head] != null && this.points[head].compareTo(point) == 0) {
                throw new IllegalArgumentException("point is repeated");
            }
            this.points[i++] = point;
        }
        this.lineSegments = new LinkedList<LineSegment>();
    }

    /**
     * Get number of segments
     * @return number of segments
     */
    public int numberOfSegments() {
        return this.lineSegments.size();
    }

    /**
     * Build segments by points
     * @return segments
     */
    public LineSegment[] segments() {
        for (int i = 0; i < this.points.length; i++) {
            Point point = this.points[i];
            Point[] clonedPoints = this.points.clone();
            Comparator<Point> comparator = point.slopeOrder();
            Arrays.sort(clonedPoints, comparator);

            int collinearPointsCount = 1;
            int k = 2; // Cause first is the current point (NEGATIVE INFINITY) and not needed to count
            LinkedList<Point> candidates = new LinkedList<Point>();
            for (; k < clonedPoints.length; k++) {
                if (comparator.compare(clonedPoints[k], clonedPoints[k - 1]) == 0) {
                    if (collinearPointsCount == 1) {
                        candidates.add(clonedPoints[k - 1]);
                    }
                    candidates.add(clonedPoints[k]);
                    collinearPointsCount++;
                    if (k + 1 < clonedPoints.length) {
                        continue;
                    }
                }

                if (collinearPointsCount == 1) {
                    continue;
                }

                if (collinearPointsCount >= COLLINEAR_POINTS_COUNT_FOR_SEGMENT && point.compareTo(candidates.peek()) < 0) {
                    this.lineSegments.add(new LineSegment(point, candidates.peekLast()));
                }
                candidates = new LinkedList<Point>();
                collinearPointsCount = 1;
            }
        }
        return this.lineSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.enableDoubleBuffering();
        // draw the points
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

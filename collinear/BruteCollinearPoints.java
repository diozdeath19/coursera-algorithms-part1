/* *****************************************************************************
 *  Name: BruteCollinearPoints
 *  Date: 6 July 2019
 *  Description: BruteCollinearPoints class
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BruteCollinearPoints {
    private Point[] points;
    private boolean isCalculated;
    private ResizingArrayQueue<Point[]> segments;


    /**
     * @param points - array of points
     * @throws IllegalArgumentException - if one of points is null or repeated
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("point is null");
        }
        for (Point point: points) {
            if (point == null) {
                throw new IllegalArgumentException("point is null");
            }
        }

        Point[] clonedPoints = clonePoints(points);
        Comparator<Point> comparator = Point::compareTo;
        MergeX.sort(clonedPoints, comparator);
        int i = 0;
        this.points = new Point[clonedPoints.length];
        for (Point point: clonedPoints) {
            int head = Math.max(i - 1, 0);
            if (this.points[head] != null && this.points[head].compareTo(point) == 0) {
                throw new IllegalArgumentException("point is repeated");
            }
            this.points[i++] = point;
        }
        this.segments = new ResizingArrayQueue<Point[]>();
    }

    private Point[] clonePoints(Point[] pointsToClone) {
        Point[] clonedPoints = new Point[pointsToClone.length];
        for (int i = 0; i < pointsToClone.length; i++) {
            clonedPoints[i] = pointsToClone[i];
        }
        return clonedPoints;
    }

    /**
     * Check whether points are connected
     * @param p - Point p
     * @param q - Point q
     * @return - boolean
     */
    private boolean isConnectedPoints(Point p, Point q) {
        for (Point[] segment: this.segments) {
            if ((segment[0].compareTo(p) == 0 && segment[1].compareTo(q) == 0) || (segment[0].compareTo(q) == 0 && segment[1].compareTo(p) == 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get number of segments
     * @return number of segments
     */
    public int numberOfSegments() {
        return this.segments.size();
    }

    /**
     * Build segments by points
     * @return segments
     */
    public LineSegment[] segments() {
        for (int p = 0; p < this.points.length; p++) {
            Comparator<Point> comparator = this.points[p].slopeOrder();
            for (int q = p+1; q < this.points.length; q++) {
                for (int r = q+1; r < this.points.length; r++) {
                    for (int s = r+1; s < this.points.length; s++) {
                        if (comparator.compare(this.points[q], this.points[r]) == 0 && comparator.compare(this.points[r], this.points[s]) == 0) {
                            if (this.isConnectedPoints(this.points[p], this.points[s])) {
                                continue;
                            }
                            Point[] segment = {this.points[p], this.points[s]};
                            this.segments.enqueue(segment);
                        }
                    }
                }
            }
        }

        LineSegment[] lineSegments = new LineSegment[this.segments.size()];
        int i = 0;
        for (Point[] segment : this.segments) {
            lineSegments[i++] = new LineSegment(segment[0], segment[1]);
        }
        return lineSegments;
    }

    /**
     * Testing
     * @param args
     */
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

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

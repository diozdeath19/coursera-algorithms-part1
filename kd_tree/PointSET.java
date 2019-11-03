import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        this.set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public int size() {
        return this.set.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        this.set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        return this.set.contains(p);
    }

    public void draw() {
        for (Point2D p: this.set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        for (Point2D p: this.set) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        Point2D nearest = null;
        for (Point2D point: this.set) {
            if (nearest == null || p.distanceTo(point) < p.distanceTo(nearest)) {
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}

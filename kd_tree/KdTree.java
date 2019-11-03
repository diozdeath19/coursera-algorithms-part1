/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {
    private class Node {
        private Point2D point;
        private boolean even;
        private Node left;
        private Node right;

        public Node(Point2D point, boolean even, Node left, Node right) {
            this.point = point;
            this.even = even;
            this.left = left;
            this.right = right;
        }

        public boolean isEven() {
            return even;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Point2D getPoint() {
            return point;
        }
    }

    private Node root;
    private int size;


    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        if (this.root == null) {
            this.root = new Node(p, false, null, null);
            this.size++;
            return;
        }
        this.insert(root, p);
    }

    private void insert(Node curNode, Point2D p) {
        Point2D curPoint = curNode.getPoint();
        if (curPoint.equals(p)) {
            return;
        }
        if (!curNode.isEven()) {
          if (p.x() < curPoint.x()) {
            this.insertInLeft(curNode, p);
          } else {
              this.insertInRight(curNode, p);
          }
        } else {
            if (p.y() < curPoint.y()) {
                this.insertInLeft(curNode, p);
            } else {
                this.insertInRight(curNode, p);
            }
        }
    }

    private void insertInLeft(Node curNode, Point2D p) {
        if (curNode.getLeft() != null) {
            this.insert(curNode.getLeft(), p);
            return;
        }
        Node left = new Node(p, !curNode.isEven(), null, null);
        curNode.setLeft(left);
        this.size++;
    }

    private void insertInRight(Node curNode, Point2D p) {
        if (curNode.getRight() != null) {
            this.insert(curNode.getRight(), p);
            return;
        }
        Node right = new Node(p, !curNode.isEven(), null, null);
        curNode.setRight(right);
        this.size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        return this.contains(root, p);
    }

    private boolean contains(Node curNode, Point2D p) {
        if (curNode == null) {
            return false;
        }
        if (curNode.getPoint().equals(p)) {
            return true;
        }
        if (!curNode.isEven()) {
            if (p.x() < curNode.getPoint().x()) {
                return this.contains(curNode.getLeft(), p);
            } else {
                return this.contains(curNode.getRight(), p);
            }
        } else {
            if (p.y() < curNode.getPoint().y()) {
                return this.contains(curNode.getLeft(), p);
            } else {
                return this.contains(curNode.getRight(), p);
            }
        }
    }

    public void draw() {
        if (this.isEmpty()) {
          return;
        }
        this.draw(root);
    }

    private void draw(Node node) {
        node.getPoint().draw();
        if (node.getLeft() != null) {
            this.draw(node.getLeft());
        }
        if (node.getRight() != null) {
            this.draw(node.getRight());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        this.range(root, rect, list);
        return list;
    }

    private void range(Node curNode, RectHV rect, ArrayList<Point2D> list) {
        if (curNode == null) {
            return;
        }

        Point2D point = curNode.getPoint();
        // If rect contains point, search in two subtrees
        if (rect.contains(point)) {
            list.add(point);
            this.range(curNode.getLeft(), rect, list);
            this.range(curNode.getRight(), rect, list);
            return;
        }

        if (!curNode.isEven()) {
            if (rect.xmax() < point.x()) {
                this.range(curNode.getLeft(), rect, list);
            } else if (rect.xmin() <= point.x() && point.x() <= rect.xmax()) {
                this.range(curNode.getLeft(), rect, list);
                this.range(curNode.getRight(), rect, list);
            } else {
                this.range(curNode.getRight(), rect, list);
            }
        } else {
            if (rect.ymax() < point.y()) {
                this.range(curNode.getLeft(), rect, list);
            } else if (rect.ymin() <= point.y() && point.y() <= rect.ymax()) {
                this.range(curNode.getLeft(), rect, list);
                this.range(curNode.getRight(), rect, list);
            } else {
                this.range(curNode.getRight(), rect, list);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        if (root == null) {
            return null;
        }
        return this.nearest(root, p);
    }

    private Point2D nearest(Node curNode, Point2D p) {
        if (curNode == null) {
            return null;
        }
        Point2D curPoint = curNode.getPoint();
        if (!curNode.isEven()) {
            if (p.x() < curPoint.x()) {
                return this.nearestInLeft(curNode, p);
            } else {
                return this.nearestInRight(curNode, p);
            }
        } else {
            if (p.y() < curPoint.y()) {
                return this.nearestInLeft(curNode, p);
            } else {
                return this.nearestInRight(curNode, p);
            }
        }
    }

    private Point2D nearestInLeft(Node curNode, Point2D p) {
        Point2D curPoint = curNode.getPoint();
        System.out.println(curPoint.toString());
        Point2D nearest = curPoint;
        Point2D candidate = this.nearest(curNode.getLeft(), p);
        if (candidate != null && candidate.distanceTo(p) < nearest.distanceTo(p)) {
            nearest = candidate;
        }
        double perpendicular = !curNode.isEven() ? Math.abs(curPoint.x() - p.x()) : Math.abs(curPoint.y() - p.y());
        if (nearest.distanceTo(p) > perpendicular) {
            candidate = this.nearest(curNode.getRight(), p);
            if (candidate != null && candidate.distanceTo(p) < nearest.distanceTo(p)) {
                nearest = candidate;
            }
        }
        return nearest;
    }

    private Point2D nearestInRight(Node curNode, Point2D p) {
        Point2D curPoint = curNode.getPoint();
        System.out.println(curPoint.toString());
        Point2D nearest = curPoint;
        Point2D candidate = this.nearest(curNode.getRight(), p);
        if (candidate != null && candidate.distanceTo(p) < nearest.distanceTo(p)) {
            nearest = candidate;
        }
        double perpendicular = !curNode.isEven() ? Math.abs(curPoint.x() - p.x()) : Math.abs(curPoint.y() - p.y());
        if (nearest.distanceTo(p) > perpendicular) {
            candidate = this.nearest(curNode.getLeft(), p);
            if (candidate != null && candidate.distanceTo(p) < nearest.distanceTo(p)) {
                nearest = candidate;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point2D[] points = new Point2D[10];
        points[0] = new Point2D(0.372, 0.497);
        points[1] = new Point2D(0.564, 0.413);
        points[2] = new Point2D(0.226, 0.577);
        points[3] = new Point2D(0.144, 0.179);
        points[4] = new Point2D(0.083, 0.51);
        points[5] = new Point2D(0.32, 0.708);
        points[6] = new Point2D(0.417 ,0.362);
        points[7] = new Point2D(0.862, 0.825);
        points[8] = new Point2D(0.785, 0.725);
        points[9] = new Point2D(0.499, 0.208);

        KdTree kdTree = new KdTree();

        for (Point2D point: points) {
            kdTree.insert(point);
        }

        kdTree.nearest(new Point2D(0.6, 0.89));
    }
}

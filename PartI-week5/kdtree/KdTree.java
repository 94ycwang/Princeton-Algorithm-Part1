/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static boolean vertical = true;
    private static boolean horizontal = false;
    private Node root = null;
    private int size = 0;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb = null;        // the left/bottom subtree
        private Node rt = null;        // the right/top subtree

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }

    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, vertical, 0, 0, 1, 1);

    }

    private Node insert(Node node, Point2D p, boolean direct,
                        double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            size++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(p, rect);
        }

        if (direct == vertical) {
            if (p.x() < node.p.x()) {
                node.lb = insert(node.lb, p, horizontal, xmin, ymin, node.p.x(), ymax);
            }
            else if (p.x() > node.p.x()) {
                node.rt = insert(node.rt, p, horizontal, node.p.x(), ymin, xmax, ymax);
            }
            else {
                if (p.y() != node.p.y())
                    node.lb = insert(node.lb, p, horizontal, xmin, ymin, node.p.x(), ymax);
            }
        }
        else {
            if (p.y() < node.p.y()) {
                node.lb = insert(node.lb, p, vertical, xmin, ymin, xmax, node.p.y());
            }
            else if (p.y() > node.p.y()) {
                node.rt = insert(node.rt, p, vertical, xmin, node.p.y(), xmax, ymax);
            }
            else {
                if (p.x() != node.p.x())
                    node.lb = insert(node.lb, p, vertical, xmin, ymin, xmax, node.p.y());
            }
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node node = root;
        boolean direct = vertical;
        double cmp;
        while (node != null) {
            if (p.x() == node.p.x() && p.y() == node.p.y()) return true;

            if (direct == vertical) cmp = p.x() - node.p.x();
            else cmp = p.y() - node.p.y();

            if (cmp <= 0) node = node.lb;
            else if (cmp > 0) node = node.rt;

            direct = !direct;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        draw(root, vertical);

    }

    private void draw(Node node, boolean direct) {
        if (node != null) {
            // Draw lines
            StdDraw.setPenRadius();
            if (direct == vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            // Draw points
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(node.p.x(), node.p.y());

            if (node.lb != null) draw(node.lb, !direct);
            if (node.rt != null) draw(node.rt, !direct);
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        range(queue, root, rect, vertical);
        return queue;
    }

    private void range(Queue<Point2D> q, Node node, RectHV rect, boolean direct) {
        if (node != null) {
            if (rect.contains(node.p)) q.enqueue(node.p);
            if (direct == vertical) {
                if (node.p.x() >= rect.xmin()) range(q, node.lb, rect, horizontal);
                if (node.p.x() < rect.xmax()) range(q, node.rt, rect, horizontal);
            }
            else {
                if (node.p.y() >= rect.ymin()) range(q, node.lb, rect, vertical);
                if (node.p.y() < rect.ymax()) range(q, node.rt, rect, vertical);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, p, root.p, vertical);
    }

    private Point2D nearest(Node node, Point2D p, Point2D pmin, boolean direct) {
        if (node != null && node.rect.distanceSquaredTo(p) < pmin.distanceSquaredTo(p)) {
            if (node.p.distanceSquaredTo(p) <= pmin.distanceSquaredTo(p))
                pmin = node.p;

            double cmp;
            if (direct == vertical) cmp = p.x() - node.p.x();
            else cmp = p.y() - node.p.y();

            if (cmp <= 0) {
                pmin = nearest(node.lb, p, pmin, !direct);
                pmin = nearest(node.rt, p, pmin, !direct);
            }
            else {
                pmin = nearest(node.rt, p, pmin, !direct);
                pmin = nearest(node.lb, p, pmin, !direct);
            }
        }
        return pmin;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}

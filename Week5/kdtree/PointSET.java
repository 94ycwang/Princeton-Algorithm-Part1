/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointset = new TreeSet<>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointset.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointset.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        pointset.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointset.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointset) StdDraw.point(p.x(), p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : pointset) {
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }
        return queue;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointset.isEmpty()) return null;
        Point2D nearestpoint = null;
        double distsquare = Double.POSITIVE_INFINITY;
        for (Point2D point : pointset) {
            if (p.distanceSquaredTo(point) < distsquare) {
                distsquare = p.distanceSquaredTo(point);
                nearestpoint = point;
            }
        }
        return nearestpoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}

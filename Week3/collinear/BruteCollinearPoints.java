/* *****************************************************************************
 *  Name:
 *  Date: 04-01-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Merge;

import java.util.ArrayList;
import java.util.Comparator;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null input!");

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Null element!");
        }

        Point[] pts = points.clone();
        Merge.sort(pts);
        for (int i = 1; i < pts.length; i++) {
            if (pts[i].slopeTo(pts[i - 1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Repeated element!");
            }
        }

        int len = pts.length;
        for (int i = 0; i < len - 3; i++) {
            Comparator<Point> comparator = pts[i].slopeOrder();
            for (int j = i + 1; j < len - 2; j++) {
                for (int k = j + 1; k < len - 1; k++) {
                    if (comparator.compare(pts[j], pts[k]) == 0) {
                        for (int m = k + 1; m < len; m++) {
                            if (comparator.compare(pts[k], pts[m]) == 0) {
                                lines.add(new LineSegment(pts[i], pts[m]));
                            }
                        }
                    }
                }
            }
        }
    }


    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] arrlines = new LineSegment[lines.size()];
        return lines.toArray(arrlines);
    }

}

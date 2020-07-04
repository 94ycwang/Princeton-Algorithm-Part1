/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null input!");

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Null element!");
        }

        Point[] pts = points.clone();
        Arrays.sort(pts);
        for (int i = 1; i < pts.length; i++) {
            if (pts[i].slopeTo(pts[i - 1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Repeated element!");
            }
        }

        int len = pts.length;
        for (int i = 0; i < len; i++) {
            Arrays.sort(pts);
            Point p0 = pts[i];
            Comparator<Point> comparator = p0.slopeOrder();
            Arrays.sort(pts, comparator);
            int count = 1;
            int flag = 0;
            for (int j = 1; j < len; j++) {
                if (p0.slopeTo(pts[j]) == p0.slopeTo(pts[j - 1])) {
                    count++;
                    if (p0.compareTo(pts[j - 1]) > 0) flag = 1;
                }
                else {
                    if (count >= 3 && flag == 0) {
                        lines.add(new LineSegment(p0, pts[j - 1]));
                    }
                    flag = 0;
                    count = 1;
                }
            }
            if (count >= 3 && flag == 0) {
                lines.add(new LineSegment(p0, pts[len - 1]));
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

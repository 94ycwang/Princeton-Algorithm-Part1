/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(picture);
        return pic;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == picture.width() - 1 || y == picture.height() - 1)
            return 1000;
        double gx = gradient(x - 1, y, x + 1, y);
        double gy = gradient(x, y - 1, x, y + 1);
        return Math.sqrt(gx + gy);
    }

    private double gradient(int x1, int y1, int x2, int y2) {
        int rgb1 = picture.getRGB(x1, y1);
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1) & 0xFF;

        int rgb2 = picture.getRGB(x2, y2);
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2) & 0xFF;

        double g = Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2);
        return g;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(false);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(true);
    }

    private int[] findSeam(boolean isvertical) {
        int w;
        int h;
        double[][] energy2D;
        if (isvertical) {
            w = width();
            h = height();
            energy2D = new double[w][h];
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++)
                    energy2D[x][y] = energy(x, y);
        }
        else {
            w = height();
            h = width();
            energy2D = new double[w][h];
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++)
                    energy2D[x][y] = energy(y, x);
        }

        int[][] edgeTo = new int[w][h];
        double[][] distTo = new double[w][h];
        for (int x = 0; x < w; x++) distTo[x][0] = 1000;
        for (int y = 1; y < h; y++)
            for (int x = 0; x < w; x++)
                distTo[x][y] = Double.POSITIVE_INFINITY;

        for (int y = 0; y < h - 1; y++) {
            for (int x = 0; x < w; x++) {
                if (x == 0 && w == 1) {
                    relax(x, y + 1, x, y, energy2D, distTo, edgeTo);
                }
                else if (x == 0) {
                    relax(x, y + 1, x, y, energy2D, distTo, edgeTo);
                    relax(x + 1, y + 1, x, y, energy2D, distTo, edgeTo);
                }
                else if (x == w - 1) {
                    relax(x - 1, y + 1, x, y, energy2D, distTo, edgeTo);
                    relax(x, y + 1, x, y, energy2D, distTo, edgeTo);
                }
                else {
                    relax(x - 1, y + 1, x, y, energy2D, distTo, edgeTo);
                    relax(x, y + 1, x, y, energy2D, distTo, edgeTo);
                    relax(x + 1, y + 1, x, y, energy2D, distTo, edgeTo);
                }
            }
        }

        int col = 0;
        double minDist = Double.MAX_VALUE;
        for (int x = 0; x < w; x++) {
            if (distTo[x][h - 1] < minDist) {
                col = x;
                minDist = distTo[x][h - 1];
            }
        }

        int[] res = new int[h];
        res[h - 1] = col;
        for (int y = h - 1; y > 0; y--) {
            res[y - 1] = edgeTo[col][y];
            col = edgeTo[col][y];
        }
        return res;
    }

    private void relax(int xw, int yw, int xv, int yv, double[][] weight, double distTo[][],
                       int edgeTo[][]) {
        if (distTo[xw][yw] > distTo[xv][yv] + weight[xw][yw]) {
            distTo[xw][yw] = distTo[xv][yv] + weight[xw][yw];
            edgeTo[xw][yw] = xv;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int w = width();
        int h = height();
        if (seam == null || h <= 1 || notValidSeam(seam, w, h))
            throw new IllegalArgumentException();

        int[][] rgb = new int[w][h - 1];
        for (int x = 0; x < w; x++) {
            int row = 0;
            for (int y = 0; y < h; y++) {
                if (seam[x] != y) {
                    rgb[x][row] = picture.getRGB(x, y);
                    row += 1;
                }
            }
        }
        resetPicture(w, h - 1, rgb);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int w = width();
        int h = height();
        if (seam == null || w <= 1 || notValidSeam(seam, h, w))
            throw new IllegalArgumentException();

        int[][] rgb = new int[w - 1][h];
        for (int y = 0; y < h; y++) {
            int col = 0;
            for (int x = 0; x < w; x++) {
                if (seam[y] != x) {
                    rgb[col][y] = picture.getRGB(x, y);
                    col += 1;
                }
            }
        }
        resetPicture(w - 1, h, rgb);

    }

    private boolean notValidSeam(int[] seam, int length, int range) {
        if (seam.length != length) return true;

        for (int i = 0; i < length; i++)
            if (seam[i] < 0 || seam[i] >= range) return true;

        for (int i = 1; i < length; i++)
            if (Math.abs(seam[i] - seam[i - 1]) > 1) return true;

        return false;
    }

    private void resetPicture(int w, int h, int[][] rgb) {
        picture = new Picture(w, h);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                picture.setRGB(x, y, rgb[x][y]);
    }

    public static void main(String[] args) {

    }
}

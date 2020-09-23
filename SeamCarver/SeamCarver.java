import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;


public class SeamCarver {
    private Picture picture;
    private int width, height;
    private double[][] distTo;
    private double[][] energy;
    private int[][] edgeTo;
    private IndexMinPQ<Double> pq;
    private int[][] color;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = picture;
        width = picture.width();
        height = picture.height();
        color = new int[width][height];
        energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color[i][j] = picture.getRGB(i, j);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pic.set(i, j, new Color(color[i][j]));
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) throw new IllegalArgumentException();
        if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
            Color x1 = new Color(color[x + 1][y]);
            Color x2 = new Color(color[x - 1][y]);
            Color y1 = new Color(color[x][y + 1]);
            Color y2 = new Color(color[x][y - 1]);
            double energyx2 = Math.pow(x1.getRed() - x2.getRed(), 2) + Math
                    .pow(x1.getBlue() - x2.getBlue(), 2) + Math
                    .pow(x1.getGreen() - x2.getGreen(), 2);
            double energyy2 = Math.pow(y1.getRed() - y2.getRed(), 2) + Math
                    .pow(y1.getBlue() - y2.getBlue(), 2) + Math
                    .pow(y1.getGreen() - y2.getGreen(), 2);
            double energy = Math.sqrt(energyx2 + energyy2);
            x1 = null;
            x2 = null;
            y1 = null;
            y2 = null;
            return energy;
        }
        else return 1000;
    }

    private int[] transform(int index) {
        int x = index % width;
        int y = index / width;
        return new int[] { x, y };
    }

    // sequence of indices for vertical seam

    public int[] findVerticalSeam() {
        distTo = new double[width][height];
        edgeTo = new int[width][height];
        pq = new IndexMinPQ<Double>(width * height);
        int[] path = new int[height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j != 0) distTo[i][j] = Double.POSITIVE_INFINITY;
                edgeTo[i][j] = 0;
                if (j == 0) {
                    distTo[i][j] = 1000;
                    pq.insert(j * width + i, energy(i, j));
                }
            }
        }
        int x = 0;
        while (!pq.isEmpty()) {
            int min = pq.delMin();
            x = transform(min)[0];
            int y = transform(min)[1];
            if (y == height - 1) break;
            if (y < height - 1) {
                relax(x, y, x, y + 1, true);
                if (x > 0) relax(x, y, x - 1, y + 1, true);
                if (x < width - 1) relax(x, y, x + 1, y + 1, true);
            }
        }

        int index = x;

        for (int i = height - 1; i >= 0; i--) {
            path[i] = index;
            index = edgeTo[index][i];
        }

        distTo = null;
        edgeTo = null;
        pq = null;
        return path;
    }


    // sequence of indices for horizontal seam

    public int[] findHorizontalSeam() {
        distTo = new double[width][height];
        edgeTo = new int[width][height];
        pq = new IndexMinPQ<Double>(width * height);
        int[] path = new int[width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                edgeTo[i][j] = 0;
                if (i != 0) distTo[i][j] = Double.POSITIVE_INFINITY;
                if (i == 0) {
                    distTo[i][j] = 1000;
                    pq.insert(j * width + i, (double) 1000);
                }
            }
        }
        int y = 0;
        while (!pq.isEmpty()) {
            int min = pq.delMin();
            int x = transform(min)[0];
            y = transform(min)[1];
            if (x == width - 1) break;
            if (x < width - 1) {
                relax(x, y, x + 1, y, false);
                if (y > 0) relax(x, y, x + 1, y - 1, false);
                if (y < height - 1) relax(x, y, x + 1, y + 1, false);
            }
        }

        int index = y;

        for (int i = width - 1; i >= 0; i--) {
            path[i] = index;
            index = edgeTo[i][index];
        }

        distTo = null;
        edgeTo = null;
        pq = null;

        return path;
    }
    // remove vertical seam from current picture


    private void relax(int x1, int y1, int x2, int y2, boolean vertical) {
        if (distTo[x2][y2] > distTo[x1][y1] + energy[x2][y2]) {
            distTo[x2][y2] = distTo[x1][y1] + energy[x2][y2];
            if (vertical) edgeTo[x2][y2] = x1;
            else edgeTo[x2][y2] = y1;
            int index = x2 + y2 * width;
            if (pq.contains(index)) pq.decreaseKey(index, distTo[x2][y2]);
            else pq.insert(index, distTo[x2][y2]);
        }
    }


    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height || width == 1) throw new IllegalArgumentException();
        for (int i = 0; i < height; i++) {
            if (seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException();
            if (i != height - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = seam[i]; j < width - 1; j++) {
                color[j][i] = color[j + 1][i];
                energy[j][i] = energy[j + 1][i];
            }
        }
        width--;
        for (int i = 0; i < height; i++) {
            for (int j = Math.max(0, seam[i] - 2); j < Math.min(width, seam[i] + 3); j++) {
                energy[j][i] = energy(j, i);
            }
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width || height == 1) throw new IllegalArgumentException();
        for (int i = 0; i < width; i++) {
            if (seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException();
            if (i != width - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = seam[i]; j < height - 1; j++) {
                color[i][j] = color[i][j + 1];
                energy[i][j] = energy[i][j + 1];
            }
        }
        height--;
        for (int i = 0; i < width; i++) {
            for (int j = Math.max(0, seam[i] - 2); j < Math.min(height, seam[i] + 3); j++) {
                energy[i][j] = energy(i, j);
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}

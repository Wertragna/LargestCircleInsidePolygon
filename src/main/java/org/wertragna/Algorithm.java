package org.wertragna;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Algorithm {
    private List<DoublePoint> points;

    public Algorithm() {
        this.points = new ArrayList<>();
    }

    public List<DoublePoint> getPoints() {
        return points;
    }

    public void addPoint(DoublePoint point) {
        points.add(point);
    }

    public void cleanPolygon() {
        points = new LinkedList<>();
    }

    private int pos(int k, DoublePoint doublePoint) {
        if (points.get(k).y > doublePoint.getY()) {
            return 1;
        } else if (points.get(k).y < doublePoint.getY()) {
            return -1;
        }
        return 0;
    }

    private boolean isInside(DoublePoint generatedPoint) {
        int i, j;
        int numberOfCuts = 0;
        double intersectionPointX;
        int a, b;
        for (i = 0; i < points.size(); i++) {
            j = (i + 1) % points.size();
            if (pos(i, generatedPoint) * pos(j, generatedPoint) < 0) {
                intersectionPointX = points.get(i).x +
                        (generatedPoint.getY() - points.get(i).getY())
                                * (points.get(j).x - points.get(i).x)
                                / (points.get(j).y - points.get(i).y);
                if (intersectionPointX > generatedPoint.getX()) {
                    numberOfCuts++;
                }
            }
            if ((points.get(i).y == generatedPoint.getY())
                    && (points.get(i).x > generatedPoint.getX())) {
                a = i - 1;
                b = i + 1;
                if (a == -1) {
                    a = points.size() - 1;
                }
                if (b == points.size()) {
                    b = 0;
                }
                if (pos(a, generatedPoint) * pos(b, generatedPoint) < 0) {
                    numberOfCuts++;
                }
                if ((points.get(j).y == generatedPoint.getY())
                        && (points.get(j).x > generatedPoint.getX())) {
                    a = i - 1;
                    b = j + 1;
                    if (a == -1) {
                        a = points.size() - 1;
                    }
                    if (b == points.size()) {
                        b = 0;
                    }
                    if (pos(a, generatedPoint) * pos(b, generatedPoint) < 0) {
                        numberOfCuts++;
                    }
                }
            }
        }
        return (numberOfCuts % 2) != 0;
    }

    private double findClosestVertexDistance(DoublePoint generatedPoint) {
        int i;
        double dp, dpm;
        dpm = sqrt((generatedPoint.getX() - points.get(0).getX())
                * (generatedPoint.getX() - points.get(0).getX())
                + (generatedPoint.getY() - points.get(0).getY())
                * (generatedPoint.getY() - points.get(0).getY()));
        for (i = 1; i < points.size(); i++) {
            dp = sqrt((generatedPoint.getX() - points.get(i).getX())
                    * (generatedPoint.getX() - points.get(i).getX())
                    + (generatedPoint.getY() - points.get(i).getY())
                    * (generatedPoint.getY() - points.get(i).getY()));
            if (dp < dpm) {
                dpm = dp;
            }
        }
        return dpm;
    }


    private double findClosestEdgeDistance(DoublePoint generatedPoint) {
        int i, j;
        double de, dem = 0;
        double m, c, m1, c1;
        int found = 0;
        for (i = 0; i < points.size(); i++) {
            j = (i + 1) % points.size();
            if (points.get(i).x == points.get(j).x) {
                if ((points.get(i).getY() - generatedPoint.getY())
                        * (points.get(j).getY() - generatedPoint.getY()) < 0) {
                    if (found == 0) {
                        dem = abs(generatedPoint.getX() - points.get(i).getX());
                        found = 1;
                    } else {
                        de = abs(generatedPoint.getX() - points.get(i).getX());
                        if (dem > de) {
                            dem = de;
                        }
                    }
                }
            } else {
                if (points.get(i).y == points.get(j).y) {
                    if ((points.get(i).x - generatedPoint.getX())
                            * (points.get(j).x - generatedPoint.getX()) < 0) {
                        if (found == 0) {
                            dem = abs(generatedPoint.getY() - points.get(i).getY());
                            found = 1;
                        } else {
                            de = abs(generatedPoint.getY() - points.get(i).getY());
                            if (dem > de) {
                                dem = de;
                            }
                        }
                    }
                } else {
                    m = (points.get(i).y - points.get(j).y) / (points.get(i).x - points.get(j).x);
                    c = points.get(i).y - m * points.get(i).x;
                    m1 = -(1 / m);
                    c1 = generatedPoint.getY() - m1 * generatedPoint.getX();
                    if ((m1 * points.get(i).x -
                            points.get(i).y + c1) * (m1 * points.get(j).x - points.get(j).y + c1) < 0) {
                        if (found == 0) {
                            dem = abs(m * generatedPoint.getX() - generatedPoint.getY() + c) / sqrt(m * m + 1);
                            found = 1;
                        } else {
                            de = abs(m * generatedPoint.getX() - generatedPoint.getY() + c) / sqrt(m * m + 1);
                            if (dem > de) {
                                dem = de;
                            }
                        }
                    }
                }
            }
        }
        return dem;
    }

    public Circle findLargestCircle() {
        int i;
        long n = 10000;
        double minx, maxx, miny, maxy;
        double dpm, dem, r = 0, maxr = 1;
        double xcm = 0, ycm = 0;
        int f = 0;
        minx = points.get(0).x;
        maxx = points.get(0).x;
        miny = points.get(0).y;
        maxy = points.get(0).y;
        for (i = 0; i < points.size(); i++) {
            if (minx > points.get(i).getX()) {
                minx = points.get(i).getX();
            }
            if (maxx < points.get(i).getX()) {
                maxx = points.get(i).getX();
            }
            if (miny > points.get(i).getY()) {
                miny = points.get(i).getY();
            }
            if (maxy < points.get(i).getY()) {
                maxy = points.get(i).getY();
            }
        }

        System.out.println(minx + " " + maxx + " " + miny + " " + maxy);
        for (i = 0; i < n; i++) {
            DoublePoint generatedPoint = new DoublePoint();
            generatedPoint.setX(ThreadLocalRandom.current().nextDouble(minx, maxx));
            generatedPoint.setY(ThreadLocalRandom.current().nextDouble(miny, maxy));
            if (isInside(generatedPoint)) {
                dpm = findClosestVertexDistance(generatedPoint);
                dem = findClosestEdgeDistance(generatedPoint);
                if (dpm < dem) {
                    r = dpm;
                } else {
                    r = dem;
                }
                if (f == 0) {
                    maxr = r;
                    xcm = generatedPoint.getX();
                    ycm = generatedPoint.getY();
                    f = 1;
                } else {
                    if (maxr < r) {
                        maxr = r;
                        xcm = generatedPoint.getX();
                        ycm = generatedPoint.getY();
                    }
                }
            }
        }
        System.out.println("Radius: " + maxr + "  " + xcm + " " + ycm);
        return new Circle(xcm, ycm, maxr);

    }
}

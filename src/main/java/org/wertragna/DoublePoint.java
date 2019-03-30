package org.wertragna;

import java.awt.*;

public class DoublePoint extends Point {
    public double x;
    public double y;


    public DoublePoint(double x, double y) {
        super.x=(int) Math.round(x);
        super.y=(int) Math.round(y);
        this.x = x;
        this.y = y;
    }

    public DoublePoint() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {

        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

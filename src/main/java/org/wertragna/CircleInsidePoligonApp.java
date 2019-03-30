package org.wertragna;

/*
 * Swing version.
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/*
 * Displays a framed area.  When the user clicks within
 * the area, this program displays a filled rectangle
 * and a string indicating the coordinates where the
 * click occurred.
 */

public class CircleInsidePoligonApp extends JApplet {
    JLabel label;
    JPanel buttonPane;
    JButton b1;
    JButton b2;
    Algorithm polygonOfPoint = new Algorithm();
    RectangleArea rectangleArea;
    MyListener myListener;

    private boolean status = false;

    //Called only when this is run as an application.
    public static void main(String[] args) {
        JFrame f = new JFrame("CircleInsidePoligonApp");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        CircleInsidePoligonApp controller = new CircleInsidePoligonApp();
        controller.buildUI(f.getContentPane());
        f.pack();
        f.setVisible(true);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Algorithm getPolygonOfPoint() {
        return polygonOfPoint;
    }

    //Called only when this is run as an applet.
    public void init() {
        buildUI(getContentPane());
    }

    void buildUI(Container container) {
        container.setLayout(new BoxLayout(container,
                BoxLayout.Y_AXIS));
        label = new JLabel("Click within the framed area.");
        container.add(label);
        rectangleArea = new RectangleArea(this);
        myListener = new MyListener(polygonOfPoint, rectangleArea);
        rectangleArea.addMouseListener(myListener);
        container.add(rectangleArea);
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        b1 = new JButton("Reset");
        b1.addActionListener(e -> {
            polygonOfPoint.cleanPolygon();
            rectangleArea.repaint();
            //updateLabel(0);
        });
        b2 = new JButton("Find Largest Rectangle");
        b2.addActionListener(e -> {
            setStatus(true);
            rectangleArea.displayedRect = 6;
            rectangleArea.repaint();
        });
        buttonPane.add(b1);
        buttonPane.add(Box.createHorizontalStrut(10)); //createHorizontalGlue();
        buttonPane.add(b2);
        container.add(buttonPane);
        rectangleArea.setAlignmentX(LEFT_ALIGNMENT);
        label.setAlignmentX(LEFT_ALIGNMENT); //unnecessary, but doesn't hurt
        buttonPane.setAlignmentX(LEFT_ALIGNMENT);
    }
}

class MyListener extends MouseInputAdapter {
    Algorithm rf;
    RectangleArea rectangleArea;

    public MyListener(Algorithm rf, RectangleArea rectangleArea) {
        this.rf = rf;
        this.rectangleArea = rectangleArea;
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        DoublePoint p = new DoublePoint(x, y);
        this.rf.addPoint(p);
        this.rectangleArea.repaint();
    }

}


class RectangleArea extends JPanel {
    public int displayedRect = 6;
    CircleInsidePoligonApp controller;
    Dimension preferredSize = new Dimension(600, 450);


    public RectangleArea(CircleInsidePoligonApp controller) {
        this.controller = controller;
        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        Border loweredBevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder
                (raisedBevel, loweredBevel);
        setBorder(compound);
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  //paint background

        final List hull = this.controller.getPolygonOfPoint().getPoints();

        Point point = null;
        Point prevPoint = null;
        for (int i = 0; i < hull.size(); i++) {
            if (i == 0) {
                prevPoint = (Point) hull.get(hull.size() - 1);
            }
            point = (Point) hull.get(i);
            g.setColor(Color.black);
            g.fillOval(point.x - 2, point.y - 2, 5, 5);
            if (prevPoint != null) {
                g.fillOval(prevPoint.x - 2, prevPoint.y - 2, 5, 5);
                g.drawLine(point.x, point.y, prevPoint.x, prevPoint.y);
            }
            prevPoint = point;
        }
        if (displayedRect == 6) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.magenta);
        }
        if (controller.isStatus() == true && controller.polygonOfPoint.getPoints().size() > 2) {
            controller.setStatus(false);
            Circle circle = controller.getPolygonOfPoint().findLargestCircle();
            g.drawOval((int) Math.round(circle.getX()) - (int) (Math.round(circle.getRadius())), (int) Math.round(circle.getY()) - (int) (Math.round(circle.getRadius())), 2 * (int) (Math.round(circle.getRadius())), 2 * (int) (Math.round(circle.getRadius())));
        }
    }
}




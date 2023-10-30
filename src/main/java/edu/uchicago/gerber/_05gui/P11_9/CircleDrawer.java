package edu.uchicago.gerber._05gui.P11_9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CircleDrawer extends JFrame {
    private Point center;
    private Point perimeter;

    public CircleDrawer() {
        setTitle("Circle Drawer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (center == null) {
                    // First click: Set the center
                    center = e.getPoint();
                } else if (perimeter == null) {
                    // Second click: Set the perimeter
                    perimeter = e.getPoint();
                    repaint();
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (center != null && perimeter != null) {
            int radius = (int) center.distance(perimeter);
            int diameter = 2 * radius;

            int x = center.x - radius;
            int y = center.y - radius;

            g.drawOval(x, y, diameter, diameter);
        }
    }

    public static void main(String[] args) {
        CircleDrawer circleDrawer = new CircleDrawer();
        circleDrawer.setVisible(true);
    }
}

package edu.uchicago.gerber._05gui.P10_5;

import javax.swing.*;
import java.awt.*;

public class HouseComponent extends JComponent {
    private int x;
    private int y;
    private int width;
    private Color color;
    public HouseComponent(int x, int y, int width, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.color = color;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        int[] xPoints = {x + width / 2, x, x + width};
        int[] yPoints = {y, y + width / 2, y + width / 2};
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, 3);
        g.drawRect(x, y + width / 2, width, width);
        g.drawRect(x + width / 4, y + width, width / 4, width / 2);
        g.drawRect(x + width * 5 / 8, y + width * 9 / 8, width / 4, width / 4);
    }
}

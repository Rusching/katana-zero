package edu.uchicago.gerber._05gui.P10_2;

import javax.swing.*;
import java.awt.*;

public class BullsEyeComponent extends JComponent {
    private int circleNum = 5;
    public BullsEyeComponent(int circleNum) {
        this.circleNum = circleNum;
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int diameter = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        int diameterDecrement = diameter / this.circleNum;

        for (int i = 0; i < this.circleNum; i++) {
            if (i % 2 == 0) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillOval(x, y, diameter, diameter);
            diameter -= diameterDecrement; // Decrease the diameter for the next circle
            x += diameterDecrement / 2; // Adjust the x-coordinate for the next circle
            y += diameterDecrement / 2; // Adjust the y-coordinate for the next circle
        }

    }
}

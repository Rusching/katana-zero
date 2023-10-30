package edu.uchicago.gerber._05gui.P10_10;

import javax.swing.*;
import java.awt.*;

public class OlympicRingsViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new OlympicRingsComponent());
        frame.setVisible(true);
    }
}

class OlympicRingsComponent extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int ringDiameter = 120;
        int ringSpacing = 20;

        drawRing(g, 100, 100, ringDiameter, Color.BLUE);
        drawRing(g, 100 + ringDiameter + ringSpacing, 100, ringDiameter, Color.BLACK);
        drawRing(g, 100 + 2 * (ringDiameter + ringSpacing), 100, ringDiameter, Color.RED);
        drawRing(g, 100 + ringDiameter / 2, 100 + ringDiameter / 2, ringDiameter, Color.YELLOW);
        drawRing(g, 100 + ringDiameter / 2 + ringDiameter + ringSpacing, 100 + ringDiameter / 2, ringDiameter, Color.GREEN);
    }

    private void drawRing(Graphics g, int x, int y, int diameter, Color color) {
        g.setColor(color);
        g.drawOval(x, y, diameter, diameter);
    }
}

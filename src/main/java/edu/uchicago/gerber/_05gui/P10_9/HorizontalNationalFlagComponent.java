package edu.uchicago.gerber._05gui.P10_9;

import javax.swing.*;
import java.awt.*;

public class HorizontalNationalFlagComponent extends JComponent {
    private Color firstColor;
    private Color secondColor;
    private Color thirdColor;

    public HorizontalNationalFlagComponent(Color firstColor, Color secondColor, Color thirdColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.thirdColor = thirdColor;
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.setPreferredSize(new Dimension(250, 150));
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        int stripeHeight = getHeight() / 3;

        if (firstColor == Color.WHITE) {
            g.setColor(Color.black);
            g.drawRect(0, 0, getWidth(), stripeHeight);
        } else {
            g.setColor(firstColor);
            g.fillRect(0, 0, getWidth(), stripeHeight);
        }
        if (secondColor == Color.WHITE) {
            g.setColor(Color.black);
            g.drawRect(0, stripeHeight, getWidth(), stripeHeight);
        } else {
            g.setColor(secondColor);
            g.fillRect(0, stripeHeight, getWidth(), stripeHeight);
        }
        if (thirdColor == Color.WHITE) {
            g.setColor(Color.black);
            g.drawRect(0, stripeHeight * 2, getWidth(), stripeHeight);
        } else {
            g.setColor(thirdColor);
            g.fillRect(0, stripeHeight * 2, getWidth(), stripeHeight);
        }
    }
}

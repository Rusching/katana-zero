package edu.uchicago.gerber._05gui.P10_9;

import javax.swing.*;
import java.awt.*;

public class HorizontalNationalFlagViewer {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(400, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

        HorizontalNationalFlagComponent germanFlag = new HorizontalNationalFlagComponent(Color.BLACK, Color.RED, Color.YELLOW);
        JPanel germanPanel = new JPanel();
        germanPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        germanPanel.add(germanFlag);
        jFrame.add(germanPanel);
        JLabel germanLabel = new JLabel("Germany");
        germanLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        germanPanel.add(germanLabel);
        jFrame.add(germanPanel);

        jFrame.add(Box.createVerticalStrut(50));

        HorizontalNationalFlagComponent hungarianFlag = new HorizontalNationalFlagComponent(Color.RED, Color.WHITE, Color.GREEN);
        JPanel hungarianPanel = new JPanel();
        hungarianPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        hungarianPanel.add(hungarianFlag);
        JLabel hungarianLabel = new JLabel("Hungary");
        hungarianLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hungarianPanel.add(hungarianLabel);
        jFrame.add(hungarianPanel);

        jFrame.setVisible(true);
    }
}

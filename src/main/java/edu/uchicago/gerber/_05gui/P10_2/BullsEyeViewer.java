package edu.uchicago.gerber._05gui.P10_2;

import javax.swing.*;

public class BullsEyeViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BullsEyeComponent bullsEye = new BullsEyeComponent(5);
        frame.add(bullsEye);

        frame.setVisible(true);

        while(true) {
            String input = JOptionPane.showInputDialog(frame, "Enter the total num of circles you want to show, 0 to exit:");
            if (input == null || input.isEmpty() || input.equals("0")) {
                break;
            }
            try {
                int numCircles = Integer.parseInt(input);
                bullsEye.setCircleNum(numCircles);
                frame.repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input.");
            }
        }
    }
}

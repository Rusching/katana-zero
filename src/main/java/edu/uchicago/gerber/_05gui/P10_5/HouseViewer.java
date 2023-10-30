package edu.uchicago.gerber._05gui.P10_5;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class HouseViewer {
    private static final Random random = new Random();

    public static Color getRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(700, 700);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setPreferredSize(new Dimension(700, 700));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the houses you want to create:");
        int houseNum = scanner.nextInt();
        int x, y, width;
        HouseComponent house;
        for (int i = 0; i < houseNum; i++) {
            System.out.println("Enter house the left corner x coordinate of house " + i + ": (between 0 and 600)");
            x = scanner.nextInt();
            System.out.println("Enter house the left corner y coordinate of house " + i + ": (between 0 and 600)");
            y = scanner.nextInt();
            System.out.println("Enter house the width of house " + i + ": (do not be too large)");
            width = scanner.nextInt();
            System.out.println("The color of the house would be random.");
            house = new HouseComponent(x, y, width, getRandomColor());
            house.setBounds(x, y, x + width, y + width * 3 / 2);
            jPanel.add(house);
        }
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}

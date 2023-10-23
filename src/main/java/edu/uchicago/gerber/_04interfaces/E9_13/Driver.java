package edu.uchicago.gerber._04interfaces.E9_13;

import edu.uchicago.gerber._04interfaces.P9_6.Appointment;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        BetterRectangle betterRectangle1 = new BetterRectangle();
        System.out.println(String.format("BetterRectangle 1: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle1.x, betterRectangle1.y, betterRectangle1.width, betterRectangle1.height, betterRectangle1.getPerimeter(), betterRectangle1.getArea()));
        BetterRectangle betterRectangle2 = new BetterRectangle(5, 7, 8, 9);
        System.out.println(String.format("BetterRectangle 2: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle2.x, betterRectangle2.y, betterRectangle2.width, betterRectangle2.height, betterRectangle2.getPerimeter(), betterRectangle2.getArea()));
        BetterRectangle betterRectangle3 = new BetterRectangle(betterRectangle2);
        System.out.println(String.format("BetterRectangle 3: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle3.x, betterRectangle3.y, betterRectangle3.width, betterRectangle3.height, betterRectangle3.getPerimeter(), betterRectangle3.getArea()));
        Scanner scanner = new Scanner(System.in);
        boolean continueTry = true;
        while (continueTry) {
            System.out.println("\nEnter the rectangle information:");

            System.out.print("x:");
            int x = scanner.nextInt();
            System.out.print("y:");
            int y = scanner.nextInt();
            System.out.print("width:");
            int width = scanner.nextInt();
            System.out.print("height:");
            int height = scanner.nextInt();
            BetterRectangle betterRectangle4 = new BetterRectangle(x, y, width, height);
            System.out.println(String.format("New BetterRectangle : x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle3.x, betterRectangle3.y, betterRectangle4.width, betterRectangle4.height, betterRectangle4.getPerimeter(), betterRectangle4.getArea()));

            System.out.println("Press 0 to quit, otherwise continue: ");
            int flagValue = scanner.nextInt();
            if (flagValue == 0) {
                continueTry = false;
            }
        }
    }
}

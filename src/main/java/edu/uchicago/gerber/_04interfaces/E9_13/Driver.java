package edu.uchicago.gerber._04interfaces.E9_13;

public class Driver {
    public static void main(String[] args) {
        BetterRectangle betterRectangle1 = new BetterRectangle();
        System.out.println(String.format("BetterRectangle 1: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle1.x, betterRectangle1.y, betterRectangle1.width, betterRectangle1.height, betterRectangle1.getPerimeter(), betterRectangle1.getArea()));
        BetterRectangle betterRectangle2 = new BetterRectangle(5, 7, 8, 9);
        System.out.println(String.format("BetterRectangle 2: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle2.x, betterRectangle2.y, betterRectangle2.width, betterRectangle2.height, betterRectangle2.getPerimeter(), betterRectangle2.getArea()));
        BetterRectangle betterRectangle3 = new BetterRectangle(betterRectangle2);
        System.out.println(String.format("BetterRectangle 3: x: %d, y: %d, width: %d, height: %d, perimeter: %d, area: %d.", betterRectangle3.x, betterRectangle3.y, betterRectangle3.width, betterRectangle3.height, betterRectangle3.getPerimeter(), betterRectangle3.getArea()));

    }
}

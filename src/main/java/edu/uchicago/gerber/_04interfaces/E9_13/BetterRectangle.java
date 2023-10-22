package edu.uchicago.gerber._04interfaces.E9_13;

import java.awt.*;

public class BetterRectangle extends Rectangle {
    public BetterRectangle() {
        setLocation(0, 0);
        setSize(0, 0);
    }

    public BetterRectangle(BetterRectangle betterRectangle) {
        setLocation(betterRectangle.x, betterRectangle.y);
        setSize(betterRectangle.width, betterRectangle.height);
    }

    public BetterRectangle(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    public int getPerimeter() {
        return (width + height) * 2;
    }

    public int getArea() {
        return width * height;
    }
}
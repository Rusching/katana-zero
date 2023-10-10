package edu.uchicago.gerber._03objects;

import lombok.Getter;

class SodaCan {
    @Getter
    private int height;
    @Getter
    private int radius;
    private static double pi = 3.1415926;
    public SodaCan(int height, int radius) {
        this.height = height;
        this.radius = radius;
    }

    public double getSurfaceArea() {
        return 2 * pi * this.radius * this.height + 2 * pi * this.radius * this.radius;
    }

    public double getVolumn() {
        return pi * this.radius * this.radius * this.height;
    }

}

public class P8_5 {
    public static void main(String[] args) {
        SodaCan sodaCan1 = new SodaCan(2, 10);
        SodaCan sodaCan2 = new SodaCan(5, 5);
        SodaCan sodaCan3 = new SodaCan(8, 6);
        System.out.println(String.format("SodaCan1 height %d, radius %d, surface area %.5f, volumn %.5f", sodaCan1.getHeight(), sodaCan1.getRadius(), sodaCan1.getSurfaceArea(), sodaCan1.getVolumn()));
        System.out.println(String.format("SodaCan2 height %d, radius %d, surface area %.5f, volumn %.5f", sodaCan2.getHeight(), sodaCan2.getRadius(), sodaCan2.getSurfaceArea(), sodaCan2.getVolumn()));
        System.out.println(String.format("SodaCan3 height %d, radius %d, surface area %.5f, volumn %.5f", sodaCan3.getHeight(), sodaCan3.getRadius(), sodaCan3.getSurfaceArea(), sodaCan3.getVolumn()));
    }
}
package edu.uchicago.gerber._04interfaces.E9_17;

import lombok.Getter;

import java.util.Map;

public class SodaCan implements Measurable {
    @Getter
    private double height;
    @Getter
    private double radius;
    private static double pi = 3.1415926;
    public SodaCan(double height, double radius) {
        this.height = height;
        this.radius = radius;
    }

    @Override
    public double measure() {
        return 2 * pi * this.radius * this.height + 2 * pi * this.radius * this.radius;
    }

    public double getVolumn() {
        return pi * this.radius * this.radius * this.height;
    }
}

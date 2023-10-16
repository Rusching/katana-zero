package edu.uchicago.gerber._03objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
class Cannonball {
    private double xPos;
    private double yPos;
    private double xSpeed;
    private double ySpeed;
    private double time;
    private final double g = -9.81;
    public Cannonball(double x) {
        this.xPos = x;
        this.yPos = 0;
    }
    public void move(double sec) {
        if (this.yPos + this.ySpeed * sec <= 0) {
            double actualSec = this.yPos / Math.abs(this.ySpeed);
            this.xPos += this.xSpeed * actualSec;
            this.yPos = 0;
            this.xSpeed = 0;
            this.ySpeed = 0;
            this.time += sec;
        } else {
            this.xPos += this.xSpeed * sec;
            this.yPos += this.ySpeed * sec;
            this.ySpeed += g * sec;
            this.time += sec;
        }
    }
    public double getX() {
        return this.xPos;
    }
    public double getY() {
        return this.yPos;
    }
    public double getTime() {
        return this.time;
    }
    public void printCurrentStatus() {
        System.out.println(String.format("Current time: %.2f secs, x pos: %.3f, y pos: %.3f", this.getTime(), this.getX(), this.getY()));
    }
    public void shoot(double angle, double velocity) {
        if (velocity <= 0) {throw new IllegalArgumentException("Initial velocity must be greater than 0.");}
        if (angle <= 0 || angle >= 180) {throw new IllegalArgumentException("Initial angle must lie between 0 and 180, both exclusive.");}
        this.xSpeed = velocity * Math.cos(Math.toRadians(angle));
        this.ySpeed = velocity * Math.sin(Math.toRadians(angle));

        this.move(0.1);
        this.printCurrentStatus();
        while (this.getY() > 0) {
            this.move(0.1);
            this.printCurrentStatus();
        }
        System.out.println(String.format("Cannonball reaches the ground with time: %.2f secs, x pos: %.3f, y pos: %.3f", this.getTime(), this.getX(), this.getY()));
    }

}
public class P8_19 {
    public static void main(String[] args) {

        System.out.println("Here is my testing data:");

        Cannonball cannonball1 = new Cannonball(0);
        cannonball1.shoot(45, 5);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the starting angle:");
        double angle = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the starting angle:");
        double velocity = scanner.nextDouble();

        Cannonball cannonball2 = new Cannonball(0);
        cannonball2.shoot(angle, velocity);
    }

}

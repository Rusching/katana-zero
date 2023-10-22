package edu.uchicago.gerber._04interfaces.E9_17;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        System.out.println("My test data: ");


        ArrayList<SodaCan> sodaCanArrayList = new ArrayList<>();
        sodaCanArrayList.add(new SodaCan(5, 8));
        sodaCanArrayList.add(new SodaCan(1, 6));
        sodaCanArrayList.add(new SodaCan(2, 15));
        sodaCanArrayList.add(new SodaCan(28, 1));
        sodaCanArrayList.add(new SodaCan(8, 8));
        double totalMeasure = 0;
        for (SodaCan sodaCan: sodaCanArrayList) {
            totalMeasure += sodaCan.measure();
        }
        System.out.println(String.format("Total %d sodaCans, total measure %.3f, average measure %.3f.", sodaCanArrayList.size(), totalMeasure, totalMeasure / sodaCanArrayList.size()));
        sodaCanArrayList.clear();

        System.out.println("Enter number of sodaCans you want to add:");
        Scanner scanner = new Scanner(System.in);
        int numberOfSodaCans = scanner.nextInt();
        scanner.nextLine(); // consume the newline
        totalMeasure = 0;
        for (int i = 0; i < numberOfSodaCans; i++) {
            System.out.println("Enter the height of sodaCan " + (i + 1) + ":");
            double height = scanner.nextDouble();

            System.out.println("Enter the radius of sodaCan " + (i + 1) + ":");
            double radius = scanner.nextDouble();

            scanner.nextLine(); // consume the newline

            sodaCanArrayList.add(new SodaCan(height, radius));
        }
        for (SodaCan sodaCan: sodaCanArrayList) {
            totalMeasure += sodaCan.measure();
        }
        System.out.println(String.format("Total %d sodaCans, total measure %.3f, average measure %.3f.", sodaCanArrayList.size(), totalMeasure, totalMeasure / sodaCanArrayList.size()));
        sodaCanArrayList.clear();
    }
}



package edu.uchicago.gerber._04interfaces.P9_1;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        System.out.println("My test data: ");
        Clock clock = new Clock();
        System.out.println(String.format("Current hour: %s, minute: %s, time: %s", clock.getHours(), clock.getMinutes(), clock.getTime()));
        WorldClock worldClock1 = new WorldClock(3);
        System.out.println(String.format("[with 3 hours offset] Current hour: %s, minute: %s, time: %s", worldClock1.getHours(), worldClock1.getMinutes(), worldClock1.getTime()));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of hours offset you want to create for a WorldClock:");
        int offset = scanner.nextInt();
        WorldClock worldClock2 = new WorldClock(offset);
        scanner.nextLine();
        System.out.println(String.format("[with %d hours offset] Current hour: %s, minute: %s, time: %s", offset, worldClock2.getHours(), worldClock2.getMinutes(), worldClock2.getTime()));

    }
}

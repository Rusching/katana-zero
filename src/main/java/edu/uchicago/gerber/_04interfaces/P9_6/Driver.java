package edu.uchicago.gerber._04interfaces.P9_6;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        appointmentArrayList.add(new Daily("daily 1"));
        appointmentArrayList.add(new Monthly(5, "monthly 1"));
        appointmentArrayList.add(new OneTime(2023, 11, 28, "one time 1"));
        appointmentArrayList.add(new Daily("daily 2"));
        appointmentArrayList.add(new Monthly(17, "monthly 2"));
        appointmentArrayList.add(new OneTime(2023, 9, 22, "one time 2"));
        appointmentArrayList.add(new Daily("daily 3"));
        appointmentArrayList.add(new Monthly(23, "monthly 3"));
        appointmentArrayList.add(new OneTime(2023, 10, 22, "one time 3"));
        appointmentArrayList.add(new Daily("daily 4"));
        appointmentArrayList.add(new Monthly(19, "monthly 4"));
        appointmentArrayList.add(new OneTime(2023, 12, 24, "one time 4"));
        appointmentArrayList.add(new Daily("daily 5"));
        appointmentArrayList.add(new Monthly(26, "monthly 5"));
        appointmentArrayList.add(new OneTime(2024, 3, 2, "one time 5"));

        Scanner scanner = new Scanner(System.in);
        boolean continueTry = true;
        while (continueTry) {
            System.out.println("\nEnter year, month, day to query all satisfied appointments.");

            System.out.print("Year:");
            int year = scanner.nextInt();
            System.out.print("Month:");
            int month = scanner.nextInt();
            System.out.print("Day:");
            int day = scanner.nextInt();

            System.out.println("Appointments on " + year + "-" + month + "-" + day + " are: ");
            for (Appointment appointment: appointmentArrayList) {
                if (appointment.occursOn(year, month, day)) {
                    System.out.println(appointment);
                }
            }
            System.out.println("Press 0 to quit, otherwise continue: ");
            int flagValue = scanner.nextInt();
            if (flagValue == 0) {
                continueTry = false;
            }
        }
    }
}

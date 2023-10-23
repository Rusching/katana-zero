package edu.uchicago.gerber._04interfaces.E9_11;

import edu.uchicago.gerber._04interfaces.E9_13.BetterRectangle;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        System.out.println("This is my test data:");

        Student student1 = new Student("Math", "John", 1996);
        System.out.println("Student 1: " + student1.toString());
        Student student2 = new Student();
        System.out.println("Student 2: " + student2.toString());
        Student student3 = new Student("Computer Science");
        System.out.println("Student 3: " + student3.toString());

        Instructor instructor1 = new Instructor(2000, "Troey", 1981);
        System.out.println("Instructor 1: " + instructor1.toString());
        Instructor instructor2 = new Instructor();
        System.out.println("Instructor 2: " + instructor2.toString());
        Instructor instructor3 = new Instructor(50000);
        System.out.println("Instructor 3: " + instructor3.toString());

        Scanner scanner = new Scanner(System.in);
        boolean continueTry = true;
        while (continueTry) {
            System.out.println("\nEnter the type you want to create: 0 for Student, 1 for Instructor:");
            int objectType = scanner.nextInt();
            scanner.nextLine();
            if (objectType != 0 && objectType != 1) {
                System.out.println("Invalid input!");
            } else {
                System.out.print("Input name: ");
                String name = scanner.nextLine();
                System.out.print("Input year of birth: ");
                int yearOfBirth = scanner.nextInt();
                scanner.nextLine();
                if (objectType == 0) {
                    System.out.print("Input major: ");
                    String major = scanner.nextLine();
                    Student sss = new Student(major, name, yearOfBirth);
                    System.out.println("User input Student : " + sss.toString());
                } else {
                    System.out.println("Input salary: ");
                    int salary = scanner.nextInt();
                    Instructor iii = new Instructor(salary, name, yearOfBirth);
                    System.out.println("User input Instructor : " + iii.toString());
                }

                System.out.println("Press 0 to quit, otherwise continue: ");
                int flagValue = scanner.nextInt();
                if (flagValue == 0) {
                    continueTry = false;
                }
            }
        }
    }
}

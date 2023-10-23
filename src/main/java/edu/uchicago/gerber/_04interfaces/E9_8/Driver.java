package edu.uchicago.gerber._04interfaces.E9_8;

import edu.uchicago.gerber._04interfaces.E9_11.Instructor;
import edu.uchicago.gerber._04interfaces.E9_11.Student;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        System.out.println("This is my test data:");
        BasicAccount basicAccount1 = new BasicAccount();
        basicAccount1.deposit(1000);
        basicAccount1.deposit(500);
        basicAccount1.withdraw(50);
        basicAccount1.withdraw(2000);
        Scanner scanner = new Scanner(System.in);

        BasicAccount basicAccount2 = new BasicAccount();
        boolean continueTry = true;
        while (continueTry) {
            System.out.println("\nEnter action: 0 for deposit, 1 for withdraw");
            int actionType = scanner.nextInt();
            scanner.nextLine();
            if (actionType != 0 && actionType != 1) {
                System.out.println("Invalid input!");
            } else {
                if (actionType == 0) {
                    System.out.println("Enter the amount you want to deposit:");
                    double amount = scanner.nextDouble();
                    basicAccount2.deposit(amount);
                } else {
                    System.out.println("Enter the amount you want to withdraw:");
                    double amount = scanner.nextDouble();
                    basicAccount2.withdraw(amount);
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

package edu.uchicago.gerber._07streams;

import java.util.Scanner;

public class E13_4 {
    public static String generateBinaryString(int n) {
        if (n == 0) return "";
        if (n % 2 == 0) {
            return generateBinaryString(n - 1) + "0";
        } else {
            return generateBinaryString(n - 1) + "1";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter the number n you want to produce the binary string: (0 to quit)");
            int n = scanner.nextInt();
            if (n == 0){
                continueFlag = false;
            } else {
                System.out.println("The binary string generated is: ");
                System.out.println(generateBinaryString(n));
            }
        }
        System.out.println("Program ended.");
    }
}

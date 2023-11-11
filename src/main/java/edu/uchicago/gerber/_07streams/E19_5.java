package edu.uchicago.gerber._07streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E19_5 {

    public static <T> String toString(Stream<T> stream, int n) {
        return stream.limit(n)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter some string, separated by space: (\"quit\" to quit)");
            String words = scanner.nextLine();
            if (words.equals("quit")) {
                continueFlag = false;
            } else {
                String[] terms = words.split(" ");
                System.out.println("Enter the number of terms you want to concat:");
                int n = scanner.nextInt();
                System.out.println(toString(Stream.of(terms), n));
                scanner.nextLine();
            }
        }
    }
}

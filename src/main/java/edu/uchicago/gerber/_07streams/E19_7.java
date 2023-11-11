package edu.uchicago.gerber._07streams;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

public class E19_7 {
    public static void main(String[] args) {
        Function<String, String> func = (String s) -> s.charAt(0) + "..." + s.charAt(s.length() - 1);

        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter some string, separated by space: (\"quit\" to quit)");
            String words = scanner.nextLine();
            if (words.equals("quit")) {
                continueFlag = false;
            } else {
                String[] terms = words.split(" ");
                System.out.println("Print processed stream as below:");
                Stream.of(terms).filter(w -> w.length() > 2)
                        .map(func)
                        .forEach(System.out::println);
            }
        }
    }
}

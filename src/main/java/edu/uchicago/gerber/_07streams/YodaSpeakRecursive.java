package edu.uchicago.gerber._07streams;

import edu.uchicago.gerber._08final.mvc.controller.GameOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class YodaSpeakRecursive {
    public static List<String> reverseWordOrdersRecursive(List<String> words) {
        if (words.size() == 1) {
            return words;
        } else {
            List<String> reversedWords = reverseWordOrdersRecursive(words.subList(1, words.size()));
            reversedWords.add(words.get(0));
            return reversedWords;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter a sentence you want to reverse: (\"quit\" to quit)");
            String sentence = scanner.nextLine();
            if (sentence.equals("quit")){
                continueFlag = false;
            } else {
                String[] numbers = sentence.split(" ");
                System.out.println("The reversed version is:");
                System.out.println(String.join(" ", reverseWordOrdersRecursive(new ArrayList<>(Arrays.asList(numbers)))));
            }
        }    }
}

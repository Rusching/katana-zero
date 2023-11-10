package edu.uchicago.gerber._07streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class YodaSpeak {
    public static ArrayList<String> reverseWordOrdersIteration(ArrayList<String> words) {
        ArrayList<String> newWords = new ArrayList<>();
        for (String word: words) {
            newWords.add(0, word);
        }
        return newWords;
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
                System.out.println(String.join(" ", reverseWordOrdersIteration(new ArrayList<>(Arrays.asList(numbers)))));
            }
        }
    }
}

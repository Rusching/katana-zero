package edu.uchicago.gerber._07streams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class P13_3 {
    public static HashMap<Character, String> phonePad;
    static {
        phonePad = new HashMap<>();
        phonePad.put('2', "ABC");
        phonePad.put('3', "DEF");
        phonePad.put('4', "GHI");
        phonePad.put('5', "JKL");
        phonePad.put('6', "MNO");
        phonePad.put('7', "PQRS");
        phonePad.put('8', "TUV");
        phonePad.put('9', "XYZ");
    }
    private static final String wordsPath = ".\\src\\main\\java\\edu\\uchicago\\gerber\\_07streams\\words";
    public static HashSet<String> wordsDict;

    public static HashMap<String, ArrayList<String>> number2word;

    static {
        wordsDict = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(wordsPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordsDict.add(line.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void findWords(String number, int idx, ArrayList<Character> combination, ArrayList<String> wordFound) {
        if (idx > number.length()) {
            return;
        }
        if (idx == number.length()) {
            StringBuilder sb = new StringBuilder();
            for (Character chr: combination) {
                sb.append(chr);
            }
            String currentWord = sb.toString();
            if (wordsDict.contains(currentWord)) wordFound.add(currentWord);
            return;
        }
        for (char chr: phonePad.get(number.charAt(idx)).toCharArray()) {
            combination.add(chr);
            findWords(number, idx + 1, combination, wordFound);
            combination.remove(combination.size() - 1);
        }
    }

    public static void findSentences(String[] numbers, int idx, ArrayList<String> combination, ArrayList<String> sentenceFound) {
        if (idx > numbers.length) {
            return;
        }
        if (idx == numbers.length) {
            sentenceFound.add(String.join(" ", combination));
            return;
        }
        for (String nextWord: number2word.get(numbers[idx])) {
            combination.add(nextWord);
            findSentences(numbers, idx + 1, combination, sentenceFound);
            combination.remove(combination.size() - 1);
        }
    }

    public static void displayPrompt() {
        System.out.println("The valid input digits are 2-9. 0 and 1 are invalid.");

    }

    public static void main(String [] args) {

        Scanner scanner = new Scanner(System.in);

        number2word = new HashMap<>();

        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter the number string you want to translate to sentences, use \"-\" to separate each word : (enter \"quit\" to quit)");

            String inputString = scanner.nextLine();
            if (inputString.equals("quit")){
                continueFlag = false;
            } else {
                if (inputString.trim().matches("^[2-9]+(-[2-9]+)*$")) {
                    String[] numbers = inputString.split("-");
                    for (String number: numbers) {
                        ArrayList<String> res = new ArrayList<>();
                        findWords(number, 0, new ArrayList<>(), res);
                        number2word.put(number, res);
                    }

                    ArrayList<String> sentences = new ArrayList<>();
                    findSentences(numbers, 0, new ArrayList<>(), sentences);

                    System.out.println("Generated " + sentences.size() + " possible sentences.");
                    if (sentences.size() > 0) {
                        System.out.println("The sentences generated are: ");

                        int i = 1;
                        for (String sentence: sentences) {
                            System.out.println("Sentence " + i + ": " + sentence);
                            i += 1;
                        }
                    }
                } else {
                    System.out.println("The input format is invalid. Please do not enter 0 or 1 or other non-digit letters; and remember use \"-\" to separate each term without spaces.");
                }

            }
        }
        System.out.println("Program ended.");
    }

}

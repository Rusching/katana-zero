package edu.uchicago.gerber._07streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E19_14 {
    private static final String filePath = ".\\src\\main\\java\\edu\\uchicago\\gerber\\_07streams\\words";

    public static void main(String[] args) {

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            ArrayList<String> words = lines.collect(Collectors.toCollection(ArrayList::new));

            Optional<String> palindrome = words.parallelStream()
                    .filter(word -> word.length() > 5)
                    .filter(word -> new StringBuilder(word).reverse().toString().equals(word))
                    .findAny();
            palindrome.ifPresent(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

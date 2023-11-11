package edu.uchicago.gerber._07streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E19_16 {
    private static final String filePath = ".\\src\\main\\java\\edu\\uchicago\\gerber\\_07streams\\words";

    public static void main(String[] args) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            Map<Character, Double> averageWordLength = lines
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .map(String::toLowerCase)
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.groupingBy(
                        word -> word.charAt(0),
                        TreeMap::new,
                        Collectors.averagingInt(String::length)
                    ));

            averageWordLength.forEach((letter, avgLength) ->
                    System.out.println("Average length for words starting with '" + letter + "': " + avgLength));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package edu.uchicago.gerber._02arrays;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class E7_4 {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Type the input file name to read:");
        String inputFileName = scan.nextLine();
        System.out.println("Type the output file name to write:");
        String outputFileName = scan.nextLine();
        // !! Note to provide the absolute path of input and output file !!


        ArrayList<String> stringArray = new ArrayList<>();
        int lineCounter = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                stringArray.add(String.format("/* %d */ ", lineCounter) + currentLine);
                lineCounter += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            String contentToWrite = String.join(System.lineSeparator(), stringArray);
            writer.write(contentToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

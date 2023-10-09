package edu.uchicago.gerber._02arrays;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
class CSVReader {

    // the data structure to store all field in all rows
    private ArrayList<ArrayList<String>> csvData = new ArrayList<>();

    public CSVReader(String inputFileName) {
        // read the csv file line by line, the same as E7_4
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                parseCSVLine(currentLine);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public int numberOfRows() {
        return csvData.size();
    }

    public int numberOfFields(int row) {
        if (row < 0 || row >= csvData.size()) {
            throw new IndexOutOfBoundsException("Invalid row index provided");
        }
        return csvData.get(row).size();
    }

    public String field(int row, int column) {
        if (row < 0 || row >= csvData.size()) {
            throw new IndexOutOfBoundsException("Invalid row index provided");
        }
        ArrayList<String> currentRow = csvData.get(row);
        if (column < 0 || column >= currentRow.size()) {
            throw new IndexOutOfBoundsException("Invalid column index provided");
        }
        return currentRow.get(column);
    }

    public void parseCSVLine(String inputString) {
        boolean isInQuote = false;
        ArrayList<String> currentRowFields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < inputString.length(); ++i) {
            if (isInQuote) {
                if (inputString.charAt(i) == '"') {
                    if ((i - 1 >= 0 && inputString.charAt(i - 1) == '"') || (i + 1 < inputString.length() && inputString.charAt(i + 1) == '"')) {
                        // if there is a quote mark immediately before or after it,
                        // then it represents it's an inner quote so isInQuote flag would not change.

                        currentField.append(inputString.charAt(i));
                    } else {
                        // else, it represents quote ends so the flag should change.

                        currentField.append(inputString.charAt(i));
                        isInQuote = false;
                    }
                } else {
                    currentField.append(inputString.charAt(i));
                }
            } else {
                if (inputString.charAt(i) == '"') {
                    // when meeting a quote and isInQuote is false, set flag to true.

                    isInQuote = true;
                    currentField.append(inputString.charAt(i));
                } else if (inputString.charAt(i) == ',') {
                    // when meeting a comma then add current field into fields then start a new one.

                    currentRowFields.add(currentField.toString());
                    currentField.setLength(0);
                } else if (!(inputString.charAt(i) == ' ' && currentField.length() == 0)) {
                    // do not add leading space into a new field.

                    currentField.append(inputString.charAt(i));
                }
            }
        }
        currentRowFields.add(currentField.toString());
        csvData.add(currentRowFields);
    }
}


public class P7_5 {
    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Type the input scv file to parse: ");
        String inputFileName = scan.nextLine();

        CSVReader csvReader = new CSVReader(inputFileName);
        System.out.println(String.format("CSV file has %d rows: ", csvReader.numberOfRows()));
        for (int i = 0; i < csvReader.numberOfRows(); i++) {
            System.out.println("Row " + i + " has " + csvReader.numberOfFields(i) + " fields.");
            for (int j = 0; j < csvReader.numberOfFields(i); j++) {
                System.out.println("Field[" + i + "][" + j + "] = " + csvReader.field(i, j));
            }
        }
    }
}

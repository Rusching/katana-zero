package edu.uchicago.gerber._02arrays;

import java.util.HashMap;
import java.util.Map;


public class P5_24 {
    public static Map<Character, Integer> romanNumberMap = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);

    public static int convertRomanNumberToDecimalNumber(String inputStr) {
        int totalNumber = 0;
        while (!inputStr.isEmpty()) {
            if ((inputStr.length() == 1) || (inputStr.length() > 1 && (romanNumberMap.get(inputStr.charAt(0)) >= romanNumberMap.get(inputStr.charAt(1))))) {
                totalNumber += romanNumberMap.get(inputStr.charAt(0));
                inputStr = inputStr.substring(1);
            }
            else {
                totalNumber += romanNumberMap.get(inputStr.charAt(1)) - romanNumberMap.get(inputStr.charAt(0));
                inputStr = inputStr.substring(2);
            }
        }
        return totalNumber;
    }

    public static void main(String[] args) {
        Map<String, Integer> testCases = new HashMap<>();
        testCases.put("I", 1);
        testCases.put("V", 5);
        testCases.put("X", 10);
        testCases.put("L", 50);
        testCases.put("C", 100);
        testCases.put("D", 500);
        testCases.put("M", 1000);
        testCases.put("IV", 4);
        testCases.put("IX", 9);
        testCases.put("XL", 40);
        testCases.put("XC", 90);
        testCases.put("CD", 400);
        testCases.put("CM", 900);
        testCases.put("VII", 7);
        testCases.put("LXXX", 80);
        testCases.put("CCVII", 207);
        testCases.put("DCCCXC", 890);
        testCases.put("MMXXIII", 2023);
        testCases.put("MCMXCIV", 1994);
        testCases.put("XII", 12);
        testCases.put("XV", 15);
        testCases.put("LXXVII", 77);
        testCases.put("XCIX", 99);
        testCases.put("CIII", 103);
        testCases.put("DLV", 555);
        testCases.put("CMXCIX", 999);
        testCases.put("IIII", 4);
        testCases.put("VIIII", 9);
        testCases.put("XXXX", 40);
        testCases.put("LXXXX", 90);
        testCases.put("CCCC", 400);
        testCases.put("DCCCC", 900);

        System.out.println(String.format("%-15s %-10s %-10s %-10s", "Testing", "Expected", "Got", "Status"));
        System.out.println("---------------------------------------------");
        for (Map.Entry<String, Integer> testCase: testCases.entrySet()) {
            int result = convertRomanNumberToDecimalNumber(testCase.getKey());
            boolean isCorrect = result == testCase.getValue();
            System.out.println(String.format("%-15s %-10d %-10d %-10s",
                    testCase.getKey(),
                    testCase.getValue(),
                    result,
                    (isCorrect ? "PASSED" : "FAILED")));
        }
    }
}

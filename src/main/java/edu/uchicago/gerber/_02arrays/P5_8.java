package edu.uchicago.gerber._02arrays;

import java.util.HashMap;
import java.util.Map;

public class P5_8 {

    public static boolean isLeapYear(int year) {
        if (year <= 0) {return false;}
        if (year % 400 == 0) {return true;}
        else if (year % 100 == 0) {return false;}
        else return year % 4 == 0;
    }

    public static void main(String[] args) {
        int b2 = 2001, b4 = 1999;

        Map<Integer, Boolean> testCases = new HashMap<>();
        testCases.put(2000, true);
        testCases.put(1996, true);
        testCases.put(2004, true);
        testCases.put(2001, false);
        testCases.put(1999, false);
        testCases.put(2005, false);
        testCases.put(1900, false);
        testCases.put(2100, false);
        testCases.put(2400, true);

        System.out.println(String.format("%-10s %-10s %-10s %-10s", "Testing", "Expected", "Got", "Status"));
        System.out.println("----------------------------------");
        for (Map.Entry<Integer, Boolean> testCase: testCases.entrySet()) {
            boolean result = isLeapYear(testCase.getKey());
            boolean isCorrect = result == testCase.getValue();
            System.out.println(String.format("%-10s %-10s %-10s %-10s",
                    testCase.getKey(),
                    testCase.getValue(),
                    result,
                    (isCorrect ? "PASSED" : "FAILED")));
        }
    }
}

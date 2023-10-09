package edu.uchicago.gerber._02arrays;


import java.util.Arrays;

public class E6_9 {
    public static boolean equals(int[] a, int[] b) {
        int arrayALength = a.length, arrayBLength = b.length;
        if (arrayALength != arrayBLength) return false;
        else {
            for (int i = 0; i < arrayALength; i++) {
                if (a[i] != b[i]) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        int[][] testArraysA = { {}, {1, 2, 3}, {1, 2, 3, 4}, {1, 2, 3, 4, 5}, null, {1, 2, 3} };
        int[][] testArraysB = { {}, {1, 2, 3, 4}, {1, 2, 4, 4}, {1, 2, 3, 4, 5}, null, null };
        boolean[] expectedResults = { true, false, false, true, true, false };
        for (int i = 0; i < testArraysA.length; i++) {
            try {
                boolean result = equals(testArraysA[i], testArraysB[i]);
                boolean isCorrect = result == expectedResults[i];
                System.out.println(String.format("%-20s %-20s %-10s %-10s",
                        Arrays.toString(testArraysA[i]),
                        Arrays.toString(testArraysB[i]),
                        expectedResults[i],
                        (isCorrect ? "PASSED" : "FAILED")
                ));
            } catch (NullPointerException e) {
                System.out.println(String.format("%-20s %-20s %-10s %-10s",
                        testArraysA[i] == null ? "null" : Arrays.toString(testArraysA[i]),
                        testArraysB[i] == null ? "null" : Arrays.toString(testArraysB[i]),
                        "N/A",
                        "Exception"
                ));
            }
        }


    }

}

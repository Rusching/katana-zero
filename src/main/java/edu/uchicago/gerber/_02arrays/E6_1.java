package edu.uchicago.gerber._02arrays;

import java.util.Random;

public class E6_1 {

    public static int[] generateRandomTenIntegers() {
        int[] array = new int[10];
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            array[i] = rand.nextInt();
        }
        return array;
    }

    public static void main(String[] args) {
        int [] randArray = generateRandomTenIntegers();
        System.out.print("Every element at an even index:    ");
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                System.out.print(randArray[i] + " ");
            }
        }
        System.out.println();

        System.out.print("Every even element:                ");
        for (int i = 0; i < 10; i++) {
            if (randArray[i] % 2 == 0) {
                System.out.print(randArray[i] + " ");
            }
        }
        System.out.println();

        System.out.print("All elements in reverse order:     ");
        for (int i = 9; i >= 0; i--) {
            System.out.print(randArray[i] + " ");
        }
        System.out.println();

        System.out.print("Only the first and last element:   ");
        System.out.print(randArray[0] + " " + randArray[9]);
    }
}

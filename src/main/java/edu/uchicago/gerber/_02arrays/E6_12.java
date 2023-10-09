package edu.uchicago.gerber._02arrays;

import java.util.Arrays;
import java.util.Random;

public class E6_12 {

    public static void main(String[] args) {
        Random rand = new Random();
        int[] randArray = new int[20];
        for (int i = 0; i < 20; i++) {
            randArray[i] = rand.nextInt(99) + 1;
        }

        System.out.println("Random array of 20 elements:");
        for (int i = 0; i < 20; i++) {
            System.out.print(randArray[i] + " ");
        }
        System.out.println();

        Arrays.sort(randArray);

        System.out.println("Sorted random array:");
        for (int i = 0; i < 20; i++) {
            System.out.print(randArray[i] + " ");
        }
    }

}

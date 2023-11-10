package edu.uchicago.gerber._07streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class E13_20 {

    public static final int [] BILLS = {100, 20, 5, 1};

    public static void findPaying(int amount, int idx, ArrayList<Integer> combination, ArrayList<ArrayList<Integer>> res) {
        if (amount == 0) {
            int currentBillsUsed = combination.size();
            for (int i = 0; i < 4 - currentBillsUsed; ++i) {
                combination.add(0);
            }
            res.add(new ArrayList<>(combination));
            for (int i = 0; i < 4 - currentBillsUsed; ++i) {
                combination.remove(combination.size() - 1);
            }
            return;
        }
        if (amount < 0 || idx >= BILLS.length) {
            return;
        }
        int currentValue = BILLS[idx];
        int maxNum = amount / currentValue;
        for (int i = 0; i <= maxNum; ++i) {
            combination.add(i);
            findPaying(amount - currentValue * i, idx + 1, combination, res);
            combination.remove(combination.size() - 1);
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter the integer amount n you want to find paying ways: (0 to quit)");
            int n = scanner.nextInt();
            if (n == 0) {
                continueFlag = false;
            } else {
                ArrayList<ArrayList<Integer>> paying = new ArrayList<>();
                ArrayList<Integer> combination = new ArrayList<>();
                findPaying(n, 0, combination, paying);
                System.out.println("There are totally " + paying.size() + " ways of paying.");
                int i = 1;
                for (ArrayList<Integer> eachPaying: paying) {
                    System.out.print("Way " + i + ":");
                    System.out.println(String.format("Using 100$: %d, 20$: %d, 5$: %d, 1$: %d ",
                            eachPaying.get(0),
                            eachPaying.get(1),
                            eachPaying.get(2),
                            eachPaying.get(3)));
                    i += 1;
                }
            }
        }
        System.out.println("Program ended.");

    }
}

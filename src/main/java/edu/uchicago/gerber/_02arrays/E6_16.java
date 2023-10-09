package edu.uchicago.gerber._02arrays;

import java.util.ArrayList;
import java.util.Scanner;

public class E6_16 {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Integer> intValues = new ArrayList<>();
        String[] intStrings = scan.nextLine().split(" ");
        int max = 0, tmp;
        for (String intString: intStrings) {
            tmp = Integer.parseInt(intString);
            if (tmp > max) max = tmp;
            intValues.add(tmp);
        }
        for (int i = max; i > 0; i--) {
            String currentLine = "";
            for (int j = 0; j < intValues.size(); j++) {
                currentLine += intValues.get(j) >= i ? "*" : " ";
            }
            System.out.println(currentLine);
        }
    }
}

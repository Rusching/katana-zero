package edu.uchicago.gerber._06design.P12_1;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    Scanner scanner;

    /**
     * Constructor and initialize the scanner object.
     */
    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    /**
     * The main execute process of the Vending Machine. In the main loop
     * it asks for the product type user want to buy, and prompt for user
     * to input the coins, then determine whether this purchase can be made.
     */
    public void execute() {
        VendingMachine vendingMachine = new VendingMachine();
        boolean executeFlag = true;
        while (executeFlag) {
            this.displayPrompt();
            System.out.println("Enter the product id you want to get: ");
            int productId = scanner.nextInt() - 1;
            ArrayList<Coin> inputCoins = new ArrayList<>();
            boolean inputFlag = true;
            int totalCents = 0;
            while (inputFlag) {
                System.out.println("1: penny; 2: nickel; 3: dime; 4. quarter; 0: finish;");
                System.out.println("Enter the type of coins you want to put in: ");
                int coinType = scanner.nextInt();
                int coinNum;

                switch (coinType) {
                    case 0:
                        inputFlag = false;
                        break;
                    case 1:
                        System.out.println("Enter the num of coins of this type: ");
                        coinNum = scanner.nextInt();
                        inputCoins.add(new Coin(1, coinNum));
                        totalCents += (coinNum);
                        break;
                    case 2:
                        System.out.println("Enter the num of coins of this type: ");
                        coinNum = scanner.nextInt();
                        inputCoins.add(new Coin(5, coinNum));
                        totalCents += (coinNum * 5);
                        break;
                    case 3:
                        System.out.println("Enter the num of coins of this type: ");
                        coinNum = scanner.nextInt();
                        inputCoins.add(new Coin(10, coinNum));
                        totalCents += (coinNum * 10);
                        break;
                    case 4:
                        System.out.println("Enter the num of coins of this type: ");
                        coinNum = scanner.nextInt();
                        inputCoins.add(new Coin(25, coinNum));
                        totalCents += (coinNum * 25);
                        break;
                }
            }
            vendingMachine.tryBuy(inputCoins, totalCents, productId);
            System.out.println("Enter 0: quit; 1: display machine statistics; 2: restock; otherwise to continue");
            int userEndInput = scanner.nextInt();
            if (userEndInput == 0) {
                executeFlag = false;
            } else if (userEndInput == 1) {
                vendingMachine.displayStats();
            } else if (userEndInput == 2) {
                vendingMachine.restock();
            }
        }
    }

    /**
     * Displays the prompt for user.
     */
    public void displayPrompt() {
        System.out.println("Welcome to use this Vending Machine.");
        System.out.println("Now there are 5 kinds of products: ");
        System.out.println("Product ID 1: Water Bottle, Price: 25 cents");
        System.out.println("Product ID 2: Chocolate Bar, Price: 150 cents");
        System.out.println("Product ID 3: Chips Bag, Price: 75 cents");
        System.out.println("Product ID 4: Soda Can, Price: 50 cents");
        System.out.println("Product ID 5: Fruit Snack, Price: 100 cents");
    }
}

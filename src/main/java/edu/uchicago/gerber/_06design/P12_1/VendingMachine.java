package edu.uchicago.gerber._06design.P12_1;

import java.util.ArrayList;

public class VendingMachine {
    private int pennyNum;
    private int nickelNum;
    private int dimeNum;
    private int quarterNum;
    private int totalCents;
    private ArrayList<Product> products;

    /**
     * Constructor of Vending Machine, initialize 5 kinds of products with
     * specific price and inventor and set all current coin counts to 0.
     */
    public VendingMachine() {
        products = new ArrayList<>();
        products.add(new Product("Water Bottle", 25, 10));
        products.add(new Product("Chocolate Bar", 150, 10));
        products.add(new Product("Chips Bag", 75, 10));
        products.add(new Product("Soda Can", 50, 10));
        products.add(new Product("Fruit Snack", 100, 10));
        this.pennyNum = 0;
        this.nickelNum = 0;
        this.dimeNum = 0;
        this.quarterNum = 0;
    }

    /**
     * Try to buy a specific product given a Coin array. If the sum of given coins
     * are equal or greater than the product's price, and the inventory of the product
     * is bigger than 0, then the purchase can succeed, otherwise the purchase would
     * fail and send the money back and print corresponding failure reason.
     * @param inputCoins an arraylist contains different coins
     * @param totalCents the sum of input coins
     * @param productId the product id want to buy
     */
    public void tryBuy(ArrayList<Coin> inputCoins, int totalCents, int productId) {
        Product currentProduct = products.get(productId);
        if (totalCents >= currentProduct.getPrice() && currentProduct.getInventory() >= 1) {
            for (Coin coin: inputCoins) {
                switch (coin.getName()) {
                    case "Penny":
                        this.pennyNum += coin.getCount();
                        this.totalCents += coin.getCount();
                        break;
                    case "Nickel":
                        this.nickelNum += coin.getCount();
                        this.totalCents += (coin.getCount() * 5);
                        break;
                    case "Dime":
                        this.dimeNum += coin.getCount();
                        this.totalCents += (coin.getCount() * 10);
                        break;
                    case "Quarter":
                        this.quarterNum += coin.getCount();
                        this.totalCents += (coin.getCount() * 25);
                        break;
                }
            }
            currentProduct.setInventory(currentProduct.getInventory() - 1);
            System.out.println("Purchase succeeded; Product " + currentProduct.getName() + " fell out.");
        } else if (currentProduct.getInventory() == 0) {
            System.out.println("Purchase failed; Inventory not enough.");
            System.out.println("Entered money being sent back.");
        } else {
            System.out.println("Purchase failed; Input money not enough.");
            System.out.println("Entered money being sent back.");
        }
    }

    /**
     * Restock the Vending Machine. Set the inventory of all products to 10,
     * and collect all the coins inside the Vending Machine and set all the
     * coins' number inside the Vending Machine to 0, and print the total
     * cents inside the Vending Machine.
     */
    public void restock() {
        System.out.println("Restock the Vending Machine; all products' inventory set to 10.");
        for (Product product: this.products) {
            product.setInventory(10);
        }
        System.out.println("Total cents received since last restock: " + this.totalCents);
        System.out.println("Penny coin numbers: " + this.pennyNum);
        System.out.println("Nickel coin numbers: " + this.nickelNum);
        System.out.println("Dime coin numbers: " + this.dimeNum);
        System.out.println("Quarter coin numbers: " + this.quarterNum);
        this.pennyNum = 0;
        this.nickelNum = 0;
        this.dimeNum = 0;
        this.quarterNum = 0;
    }

    /**
     * Display the current statistics, including the inventory of each product
     * and the coin numbers inside the Vending Machine.
     */
    public void displayStats() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Current inventory in Vending Machine: ");
        for (Product product: products) {
            System.out.println(String.format("Product Name: %s, Inventory: %d", product.getName(), product.getInventory()));
        }
        System.out.println("Penny numbers in Vending Machine: " + this.pennyNum);
        System.out.println("Nickel numbers in Vending Machine: " + this.nickelNum);
        System.out.println("Dime numbers in Vending Machine: " + this.dimeNum);
        System.out.println("Quarter numbers in Vending Machine: " + this.quarterNum);
        System.out.println("Total cents in Vending Machine: " + this.totalCents);
        System.out.println("-----------------------------------------------------------");
    }

}

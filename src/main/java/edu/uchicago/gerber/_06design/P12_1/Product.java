package edu.uchicago.gerber._06design.P12_1;

public class Product {
    private String name;
    private int price;
    private int inventory;

    /**
     * Constructor that sets the name, price and inventory.
     * @param name the name of the product.
     * @param price the price of the product.
     * @param inventory the inventory of the product.
     */
    public Product(String name, int price, int inventory) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
    }

    /**
     * Returns a string represents the product.
     * @return a string represents the product.
     */
    public String toString() {
        return String.format("Product Name: %s, Product Price: %d Cents, Inventory: %d.", this.name, this.price, this.inventory);
    }

    /**
     * Returns the price of the product.
     * @return the price of the product.
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Returns the name of the product.
     * @return the name of the product.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the inventory of the product.
     * @return the inventory of the product.
     */
    public int getInventory() {
        return this.inventory;
    }

    /**
     * Set the inventory of the product.
     * @param inventory the new inventory to be set.
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}

package edu.uchicago.gerber._06design.P12_1;

public class Coin {
    private int denomination;
    private String name;
    private int count;

    /**
     * Constructor of the Coin class, and set the denomination
     * and name and count.
     * @param denomination the denomination of the coin type
     * @param count the counts of the coins input
     */
    public Coin(int denomination, int count) {
        if (denomination == 1) {
            this.name = "Penny";
            this.denomination = denomination;
        } else if (denomination == 5) {
            this.name = "Nickel";
            this.denomination = denomination;
        } else if (denomination == 10) {
            this.name = "Dime";
            this.denomination = denomination;
        } else if (denomination == 25) {
            this.name = "Quarter";
            this.denomination = denomination;
        } else {
            throw new IllegalArgumentException("The denomination must be 1, 5, 10, 25");
        }
        this.count = count;
    }

    /**
     * Returns the name of the coin.
     * @return the name of the coin.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the count of the coin.
     * @return the count of the coin.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Override the toString method that returns a string
     * when printing.
     * @return a string representing the coin.
     */
    public String toString() {
        return String.format("Coin Name: %s, Coin Denomination: %d Cents.", this.name, this.denomination);
    }
}

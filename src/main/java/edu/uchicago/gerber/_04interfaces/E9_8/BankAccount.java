package edu.uchicago.gerber._04interfaces.E9_8;

public class BankAccount {
    private double balance;
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than 0. Deposit failed.");
            return;
        }
        balance += amount;
        System.out.println(String.format("Deposit %.2f, remaining %.2f.", amount, balance));
    }
    public void withdraw(double amount) {
        balance -= amount;
    }
    public void monthEnd() {

    }
    public double getBalance() {
        return this.balance;
    }


}

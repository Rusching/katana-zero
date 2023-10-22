package edu.uchicago.gerber._04interfaces.E9_8;

public class Driver {
    public static void main(String[] args) {
        BasicAccount basicAccount = new BasicAccount();
        basicAccount.deposit(1000);
        basicAccount.deposit(500);
        basicAccount.withdraw(50);
        basicAccount.withdraw(2000);
    }
}

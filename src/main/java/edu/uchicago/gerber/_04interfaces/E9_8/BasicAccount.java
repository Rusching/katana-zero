package edu.uchicago.gerber._04interfaces.E9_8;

public class BasicAccount extends BankAccount{
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be greater than 0. Withdraw failed.");
            return;
        }
        double balanceBeforeWithdraw = getBalance();
        if (amount >= balanceBeforeWithdraw) {
            System.out.println(String.format("Tried to withdraw %.2f more than the balance %.2f.", amount, getBalance()));
            super.withdraw(balanceBeforeWithdraw);
            System.out.println(String.format("Withdrew %.2f, remaining %.2f.", balanceBeforeWithdraw, 0.0));
        } else {
            super.withdraw(amount);
            System.out.println(String.format("Withdrew %.2f, remaining %.2f.", amount, getBalance()));
        }
    }
}

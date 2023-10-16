package edu.uchicago.gerber._03objects;


import java.util.Scanner;

class MicrowaveControlPanel {
    private int executeTime;
    private int powerLevel;

    public MicrowaveControlPanel() {
        this.executeTime = 0;
        this.powerLevel = 1;
    }
    public void increaseButton() {
        this.executeTime += 30;
        System.out.println(String.format("Execute time increased by %d, becomes %d", 30, this.executeTime));
    }

    public void switchLevelButton() {
        // every time call this method,
        // powerLevel would switch between 1 and 2
        this.powerLevel = 3 - this.powerLevel;
        System.out.println(String.format("Level switched to %d", this.powerLevel));
    }

    public void resetButton() {
        this.executeTime = 0;
        this.powerLevel = 1;
        System.out.println("Microwave reset. Now with execute time 0 and power level 1.");
    }

    public void startButton() {
        System.out.println(String.format("Cooking for %d seconds at level %d", this.executeTime, this.powerLevel));
    }
}

public class P8_1 {
    public static void main(String[] args) {
        System.out.println("Here are my testing data:");
        System.out.println();
        MicrowaveControlPanel microwave = new MicrowaveControlPanel();
        microwave.increaseButton();
        microwave.increaseButton();
        microwave.startButton();
        microwave.resetButton();
        microwave.startButton();
        microwave.resetButton();
        microwave.switchLevelButton();
        microwave.increaseButton();
        microwave.startButton();
        microwave.resetButton();
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter your option: ");
            System.out.println("1: increase button");
            System.out.println("2: switch level button");
            System.out.println("3: reset button");
            System.out.println("4: start button");
            System.out.println("0: quit");
            int inputNum = scanner.nextInt();
            if (inputNum == 1) {
                microwave.increaseButton();
            } else if (inputNum == 2) {
                microwave.switchLevelButton();
            } else if (inputNum == 3) {
                microwave.resetButton();
            } else if (inputNum == 4) {
                microwave.startButton();
            } else if (inputNum == 0) {
                continueFlag = false;
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}

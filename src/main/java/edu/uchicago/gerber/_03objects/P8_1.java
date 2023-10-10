package edu.uchicago.gerber._03objects;


class MicrowaveControlPanel {
    private int executeTime;
    private int powerLevel;

    public MicrowaveControlPanel() {
        this.executeTime = 0;
        this.powerLevel = 1;
    }
    public void increaseButton() {
        this.executeTime += 30;
    }

    public void switchLevelButton() {
        // every time call this method,
        // powerLevel would switch between 1 and 2
        this.powerLevel = 3 - this.powerLevel;
    }

    public void resetButton() {
        this.executeTime = 0;
        this.powerLevel = 1;
    }

    public void startButton() {
        System.out.println(String.format("Cooking for %d seconds at level %d", this.executeTime, this.powerLevel));
    }
}

public class P8_1 {
    public static void main(String[] args) {
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
    }
}

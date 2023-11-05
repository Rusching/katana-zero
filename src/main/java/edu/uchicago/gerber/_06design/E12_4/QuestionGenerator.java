package edu.uchicago.gerber._06design.E12_4;

import java.util.Random;

public class QuestionGenerator {
    private int currentLevel;

    /**
     * Constructor of QuestionGenerator
     */
    public QuestionGenerator() {
        currentLevel = 0;
    }

    /**
     * Generates a random question at current level.
     * level 1: addition of 2 1-digit num whose sum is less than 10
     * level 2: addition of 2 arbitrary 1-digit num
     * level 3: subtraction of 2 1-digit num with a non-negative difference
     * Print the generated question to the console, and
     * returns the corresponding correct answer.
     * @return the corresponding correct answer.
     */
    public int generateQuestion() {
        int num1, num2, ans;
        Random random = new Random();
        if (currentLevel == 0) {
            // level 1: addition of 2 1-digit num whose sum is less than 10
            num1 = random.nextInt(10);
            num2 = random.nextInt(10);
            while (num1 + num2 >= 10) {
                num2 = random.nextInt(10);
            }
            System.out.println(String.format("%d + %d = ? Enter you answer below: (enter -1 for current info) ", num1, num2));
            ans = num1 + num2;
        } else if (currentLevel == 1) {
            // level 2: addition of 2 arbitrary 1-digit num

            num1 = random.nextInt(10);
            num2 = random.nextInt(10);
            System.out.println(String.format("%d + %d = ? Enter you answer below: (enter -1 for current info)", num1, num2));
            ans = num1 + num2;
        } else {
            // level 3: subtraction of 2 1-digit num with a non-negative difference

            num1 = random.nextInt(10);
            num2 = random.nextInt(10);
            while (num1 - num2 < 0) {
                num2 = random.nextInt(10);
            }
            System.out.println(String.format("%d - %d = ? Enter you answer below: (enter -1 for current info) ", num1, num2));
            ans = num1 - num2;
        }
        return ans;
    }

    /**
     * Set the current question difficulty level
     * @param newLevel the new level need to be set
     */
    public void setCurrentLevel(int newLevel) {
        if (newLevel == 0 || newLevel == 1 || newLevel == 2) {
            this.currentLevel = newLevel;
        } else {
            throw new IllegalArgumentException("new level must be in 0, 1, 2");
        }
    }

}

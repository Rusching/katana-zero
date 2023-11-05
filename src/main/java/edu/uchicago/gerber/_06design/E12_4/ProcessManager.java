package edu.uchicago.gerber._06design.E12_4;

import java.util.Scanner;

public class ProcessManager {
    private int score;
    private int tries;
    private int currentLevel;
    QuestionGenerator questionGenerator;
    Scanner scanner;
    /**
     * Constructor of ProcessManager
     */
    public ProcessManager() {
        this.score = 0;
        this.tries = 1;
        this.currentLevel = 0;
        questionGenerator = new QuestionGenerator();
        scanner = new Scanner(System.in);
    }

    /**
     * The main test process, managing the scores, levels of questions
     * and times of tries. For every question user has 2 tries; Every
     * correct answer would earn one score. When score up to 5 can upgrade
     * to next level. The highest level is 3; initial level is 1.
     */
    public void testProcess() {
        while (this.tries > 0) {
            System.out.println("Current level: " + (this.currentLevel + 1));
            System.out.println("-------------------------------------------------------");
            System.out.println("You have " + (this.tries + 1) + " chances now.");
            int ans = questionGenerator.generateQuestion();
            int userAns = scanner.nextInt();
            while (userAns == -1) {
                this.display();
                userAns = scanner.nextInt();
            }
            while (ans != userAns && this.tries > 0) {
                this.tries -= 1;
                System.out.println("Wrong answer. You have " + (this.tries + 1) + " chances now.");
                userAns = scanner.nextInt();
            }
            if (ans == userAns) {
                this.tries = 1;
                this.score += 1;
                System.out.println("Great! Your answer is correct.");
                if (this.score == 5) {
                    if (this.currentLevel < 2) {
                        this.currentLevel += 1;
                        questionGenerator.setCurrentLevel(this.currentLevel);
                    }
                    System.out.println("Congratulations! Your level improved from " + this.currentLevel + " to " + (this.currentLevel + 1));
                    System.out.println("-------------------------------------------------------");
                    this.score = 0;
                }
            } else {
                System.out.println("Chances ran out. Test failed, highest level: " + (this.currentLevel + 1));
                System.out.println("Have a good day!");
            }
        }
    }

    /**
     * Displays the current information of user test.
     */
    public void display() {
        System.out.println("--------------------------------------------");
        System.out.println("Current level: " + this.currentLevel + 1);
        System.out.println("Current score at current level: " + this.score);
        System.out.println("Remaining try times for current question: " + this.tries);
        System.out.println("Continue to enter the answer of previous question: ");
    }
}

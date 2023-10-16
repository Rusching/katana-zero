package edu.uchicago.gerber._03objects;

import java.util.Scanner;

class VotingMachine {
    private int democratVotes = 0;
    private int republicanVotes = 0;
    public VotingMachine() {

    }

    public void voteForDemocrat() {
        this.democratVotes += 1;
        System.out.println(String.format("Voted 1 votes for Democrat, now total %d", 1, this.democratVotes));
    }
    public void voteForDemocrat(int votes) {
        if (votes <= 0) throw new IllegalArgumentException("Votes must be greater 0.");
        this.democratVotes += votes;
        System.out.println(String.format("Voted %d votes for Democrat, now total %d", votes, this.democratVotes));
    }
    public void voteFotRepublican() {
        this.republicanVotes += 1;
        System.out.println(String.format("Voted 1 votes for Republican, now total %d", 1, this.republicanVotes));

    }
    public void voteFotRepublican(int votes) {
        if (votes <= 0) throw new IllegalArgumentException("Votes must be greater 0.");
        this.republicanVotes += votes;
        System.out.println(String.format("Voted %d votes for Republican, now total %d", votes, this.republicanVotes));
    }
    public int getDemocratVotes() {
        return this.democratVotes;
    }
    public int getRepublicanVotes() {
        return this.republicanVotes;
    }
    public void getTallies() {
        System.out.println(String.format("Democrat got %d votes, Republican got %d votes.", this.democratVotes, this.republicanVotes));
        if (this.democratVotes == this.republicanVotes) {
            System.out.println("Tie.");
        } else {
            System.out.println(String.format("%s leads.", this.democratVotes > this.republicanVotes? "Democrat" : "Republican"));
        }
    }

}
public class P8_8 {
    public static void main(String[] args) {
        System.out.println("Here is my testing data:");

        VotingMachine votingMachine = new VotingMachine();
        votingMachine.voteForDemocrat();
        votingMachine.voteForDemocrat();
        votingMachine.voteForDemocrat(15);
        votingMachine.getTallies();
        votingMachine.voteFotRepublican(2);
        votingMachine.voteFotRepublican(20);
        votingMachine.getTallies();
        votingMachine.voteForDemocrat(10);
        votingMachine.getTallies();

        System.out.println();

        System.out.println("Here are user input test:");
        VotingMachine machine2 = new VotingMachine();
        Scanner scanner = new Scanner(System.in);
        boolean continueFlag = true;
        while (continueFlag) {
            System.out.println("Enter your option: ");
            System.out.println("1: vote for Democrat");
            System.out.println("2: vote for Republican");
            System.out.println("3: get Tallies");
            System.out.println("0: quit");
            int inputNum = scanner.nextInt();
            int votes;
            if (inputNum == 1) {
                System.out.println("How many votes to add to Democrat? ");
                votes = scanner.nextInt();
                machine2.voteForDemocrat(votes);
            } else if (inputNum == 2) {
                System.out.println("How many votes to add to Republican? ");
                votes = scanner.nextInt();
                machine2.voteFotRepublican(votes);
            } else if (inputNum == 3) {
                machine2.getTallies();
            } else if (inputNum == 0) {
                continueFlag = false;
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}

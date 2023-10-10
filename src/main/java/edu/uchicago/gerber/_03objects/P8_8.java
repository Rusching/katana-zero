package edu.uchicago.gerber._03objects;

class VotingMachine {
    private int democratVotes = 0;
    private int republicanVotes = 0;
    public VotingMachine() {

    }

    public void voteForDemocrat() {
        this.democratVotes += 1;
    }
    public void voteForDemocrat(int votes) {
        if (votes <= 0) throw new IllegalArgumentException("Votes must be greater 0.");
        this.democratVotes += votes;
    }
    public void voteFotRepublican() {
        this.republicanVotes += 1;
    }
    public void voteFotRepublican(int votes) {
        if (votes <= 0) throw new IllegalArgumentException("Votes must be greater 0.");
        this.republicanVotes += votes;
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
    }
}

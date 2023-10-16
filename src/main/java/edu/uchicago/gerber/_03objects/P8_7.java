package edu.uchicago.gerber._03objects;


import java.util.ArrayList;
import java.util.Scanner;

class ComboLock {
    private final int secret1;
    private final int secret2;
    private final int secret3;
    private int currentTick;

    // use ticks to store previous ticks
    private ArrayList<Integer> ticks;
    // use direction to store previous directions, false: left, true: right
    private ArrayList<Boolean> directions;
    public ComboLock(int secret1, int secret2, int secret3) {
        if (secret1 < 0 || secret1 > 39 || secret2 < 0 || secret2 > 39 || secret3 < 0 || secret3 > 39) {
            throw new IllegalArgumentException("Secrets must be integers between 0 - 39");
        }
        this.secret1 = secret1;
        this.secret2 = secret2;
        this.secret3 = secret3;
        this.currentTick = 0;
        this.ticks = new ArrayList<>();
        this.directions = new ArrayList<>();
    }
    public void reset() {
        this.currentTick = 0;
        this.ticks.clear();
        this.directions.clear();
    }

    public void turnLeft(int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("Ticks must be greater than 0.");

        this.currentTick = (this.currentTick + (ticks / 40 + 1) * 40 - ticks) % 40;
        this.ticks.add(this.currentTick);
        this.directions.add(false);
    }

    public void turnRight(int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("Ticks must be greater than 0.");
        this.currentTick = (this.currentTick + ticks) % 40;
        this.ticks.add(this.currentTick);
        this.directions.add(true);
    }

    public boolean open() {
        if (this.ticks.size() == 3) {
            if (this.ticks.get(0) == this.secret1 && this.ticks.get(1) == this.secret2 && this.ticks.get(2) == this.secret3
            && this.directions.get(0) && !this.directions.get(1) && this.directions.get(2)) {
                System.out.println("Lock opened.");
                this.reset();
                return true;
            }
        }
        System.out.println("Lock didn't open.");
        return false;
    }
}
public class P8_7 {
    public static void main(String[] args) {
//        Here are some data for testing purpose:

//        ComboLock lock = new ComboLock(3, 32, 17);
//        lock.turnLeft(40);
//        lock.turnLeft(5);
//        lock.turnRight(18);
//        lock.open();
//        lock.reset();
//        lock.turnRight(3);
//        lock.turnLeft(11);
//        lock.turnRight(25);
//        lock.open();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Set your combination lock (three numbers between 0 and 39):");

        System.out.print("First number: ");
        int secret1 = scanner.nextInt();

        System.out.print("Second number: ");
        int secret2 = scanner.nextInt();

        System.out.print("Third number: ");
        int secret3 = scanner.nextInt();

        ComboLock lock2 = new ComboLock(secret1, secret2, secret3);

        boolean continueTry = true;
        while (continueTry) {
            System.out.println("\nTry to open the lock:");

            System.out.print("First turn right to: ");
            lock2.turnRight(scanner.nextInt());

            System.out.print("Then turn left to: ");
            lock2.turnLeft(scanner.nextInt());

            System.out.print("Finally, turn right to: ");
            lock2.turnRight(scanner.nextInt());

            if (lock2.open()) {
                continueTry = false;
            } else {
                System.out.print("Open failed, would like to reset and re-try? enter 0 to quit, others to continue.");
                int flagValue = scanner.nextInt();
                if (flagValue == 0) {
                    continueTry = false;
                }
            }
        }
    }

}

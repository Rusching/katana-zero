package edu.uchicago.gerber._03objects;


import java.util.ArrayList;

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
        ComboLock lock = new ComboLock(3, 32, 17);
        lock.turnLeft(40);
        lock.turnLeft(5);
        lock.turnRight(18);
        lock.open();
        lock.reset();
        lock.turnRight(3);
        lock.turnLeft(11);
        lock.turnRight(25);
        lock.open();
    }

}

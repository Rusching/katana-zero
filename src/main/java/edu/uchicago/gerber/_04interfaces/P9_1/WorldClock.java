package edu.uchicago.gerber._04interfaces.P9_1;

public class WorldClock extends Clock {
    private static final int HOURS_IN_A_DAY = 24;
    private int timeOffset;
    public WorldClock(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    @Override
    public String getHours() {
        int currentHour = (Integer.parseInt(super.getHours()) + timeOffset) % HOURS_IN_A_DAY;
        if (currentHour < 10) {
            return "0" + currentHour;
        } else{
            return Integer.toString(currentHour);
        }
    }

    @Override
    public String getMinutes() {
        return super.getMinutes();
    }

    @Override
    public String getTime() {
        return super.getTime();
    }
}

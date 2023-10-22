package edu.uchicago.gerber._04interfaces.P9_1;

public class Clock {
    public String getHours() {
        return java.time.LocalTime.now().toString().substring(0, 2);
    }

    public String getMinutes() {
        return java.time.LocalTime.now().toString().substring(3, 5);
    }

    public String getTime() {
        return getHours() + ":" + getMinutes();
    }

}

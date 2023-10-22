package edu.uchicago.gerber._04interfaces.P9_6;

public class Appointment {
    protected String description;
    protected int year;
    protected int month;
    protected int day;
    public Appointment(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = "Default description";
    }
    public Appointment(int year, int month, int day, String description) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = description;
    }

    public boolean occursOn(int year, int month, int day) {
        if (this.year == year && this.month == month && this.day == day) {
            return true;
        } else {
            return false;
        }
    }

}

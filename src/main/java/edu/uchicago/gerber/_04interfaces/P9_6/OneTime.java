package edu.uchicago.gerber._04interfaces.P9_6;

public class OneTime extends Appointment{
    public OneTime(int year, int month, int day, String description){
        super(year, month, day, description);
    }

    @Override
    public boolean occursOn(int year, int month, int day) {
        return super.occursOn(year, month, day);
    }
    @Override
    public String toString(){
        return "OneTime appointment, description: " + this.description;
    }
}

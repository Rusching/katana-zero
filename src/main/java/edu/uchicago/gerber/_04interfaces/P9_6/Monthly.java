package edu.uchicago.gerber._04interfaces.P9_6;

public class Monthly extends Appointment{
    public Monthly(int day, String description){
        super(0, 0, day, description);
    }

    @Override
    public boolean occursOn(int year, int month, int day) {
        if (this.day == day) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString(){
        return "Monthly appointment, description: " + this.description;
    }
}

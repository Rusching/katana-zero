package edu.uchicago.gerber._04interfaces.P9_6;

public class Daily extends Appointment{
//    public Daily(int year, int month, int day){
//        super(year, month, day);
//    }
    public Daily(String description) {
        super(0, 0, 0, description);
    }
    @Override
    public boolean occursOn(int year, int month, int day) {
        return true;
    }
    @Override
    public String toString(){
        return "Daily appointment, description: " + this.description;
    }
}

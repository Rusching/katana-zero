package edu.uchicago.gerber._04interfaces.E9_11;

public class Instructor extends Person {
    private double salary;
    public Instructor() {
        salary = 10000;
    }
    public Instructor(double salary) {
        this.salary = salary;
    }
    public Instructor(double salary, String name, int yearOfBirth) {
        super(name, yearOfBirth);
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Type: Instructor, Salary: %.2f", salary);
    }
}

package edu.uchicago.gerber._04interfaces.E9_11;

public class Person {
    private String name;
    private int yearOfBirth;
    public Person () {
        name = "DefaultName";
        yearOfBirth = 1970;
    }
    public Person(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }
    public String toString() {
        return String.format("Name: %s YearOfBirth: %d", name, yearOfBirth);
    }













}

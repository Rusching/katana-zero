package edu.uchicago.gerber._04interfaces.E9_11;

public class Student extends Person {
    private String major;
    public Student() {
        super();
        major = "DefaultMajor";
    }

    public Student(String major) {
        super();
        this.major = major;
    }

    public Student(String major, String name, int yearOfBirth) {
        super(name, yearOfBirth);
        this.major = major;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Type: Student, Major: %s", major);
    }
}

package edu.uchicago.gerber._04interfaces.E9_11;

public class Driver {
    public static void main(String[] args) {
        Student student1 = new Student("Math", "John", 1996);
        System.out.println("Student 1: " + student1.toString());
        Student student2 = new Student();
        System.out.println("Student 2: " + student2.toString());
        Student student3 = new Student("Computer Science");
        System.out.println("Student 3: " + student3.toString());

        Instructor instructor1 = new Instructor(2000, "Troey", 1981);
        System.out.println("Instructor 1: " + instructor1.toString());
        Instructor instructor2 = new Instructor();
        System.out.println("Instructor 2: " + instructor2.toString());
        Instructor instructor3 = new Instructor(50000);
        System.out.println("Instructor 3: " + instructor3.toString());
    }
}

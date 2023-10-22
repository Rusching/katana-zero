package edu.uchicago.gerber._03objects;

public class Person {
    private String name;
    private int id;
    public Person() {

    }

    public String getName() {
        return this.name;
    }

    public static void main(String[] args) {
        Person a = new Person();
        System.out.println(a.getName());

    }
}



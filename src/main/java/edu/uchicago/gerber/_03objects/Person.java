package edu.uchicago.gerber._03objects;

import java.util.Date;


class SwapTest {
    public static void swap(Person a, Person b) {
        String tmp = b.name;
        b.name = a.name;
        Math.abs(1);
        a.name = tmp;
    }
}


public class Person {
    public String name;
    public Person (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }



    public static void main(String[] args) {
        Person a = new Person("aaa");
        Person b = new Person("bbb");

        SwapTest.swap(a, b);

        System.out.println(a);
        System.out.println(b);

    }
}

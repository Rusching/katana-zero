package edu.uchicago.gerber._07streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E19_5 {

    public static <T> String toString(Stream<T> stream, int n) {
        List<> res = stream.limit(n)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {


    }
}

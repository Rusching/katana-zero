package edu.uchicago.gerber._07streams;

import java.util.Currency;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class E19_6 {


    public static void main(String[] args) {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        currencies.stream()
                .map(Objects::toString)
                .sorted()
                .forEach(w -> System.out.print(w + " "));
    }
}

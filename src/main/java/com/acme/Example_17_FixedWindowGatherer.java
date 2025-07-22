package com.acme;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class Example_17_FixedWindowGatherer {

    public static void main(String[] args) {
        var strings = List.of("one", "two", "three", "four", "five");

        Gatherer<String, ?, List<String>> fixedWindow =
                Gatherers.windowFixed(2);

        var result = strings.stream()
                .gather(fixedWindow)
                .toList();

        System.out.println("result = " + result);
    }
}

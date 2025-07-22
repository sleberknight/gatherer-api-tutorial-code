package com.acme;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class Example_18_SlidingWindowGatherer {

    public static void main(String[] args) {
        var strings = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Gatherer<Integer, ?, List<Integer>> createSlidingWindows =
                Gatherers.windowSliding(3);

        var result = strings.stream()
                .gather(createSlidingWindows)
                .toList();

        System.out.println("result = " + result);
    }
}

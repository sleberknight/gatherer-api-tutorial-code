package com.acme;

import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_03_FilteringGatherer {

    static <E> Gatherer<E, ?, E> filtering(Predicate<E> filter) {
        Gatherer.Integrator<Void, E, E> integrator = (_, element, downstream) -> {
            if (filter.test(element)) {
                return downstream.push(element);
            } else {
                return true; // not exactly correct...see article
            }
        };
        Gatherer<E, ?, E> gatherer = Gatherer.of(integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        Predicate<String> filter = s -> s.length() == 3;
        var filteringGatherer = filtering(filter);
        var result = Stream.of("one", "two", "three")
                .gather(filteringGatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

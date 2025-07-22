package com.acme;

import java.util.HashSet;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_09_RemoveDupsGatherer_Fixed {

    static <E> Gatherer<E, ?, E> distinct() {
        Supplier<HashSet<E>> initializer = HashSet::new;
        Gatherer.Integrator<HashSet<E>, E, E> integrator = (set, element, downstream) -> {
            if (set.add(element)) {
                return downstream.push(element);  // this fixes things by returning the value from the call to push...
            }
            return true;
        };
        Gatherer<E, ?, E> gatherer = Gatherer.ofSequential(initializer, integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        var result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 5, 5, 1)
                .gather(distinct())
                .toList();
        System.out.println("result = " + result);
    }
}

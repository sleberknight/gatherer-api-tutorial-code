package com.acme;

import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_10_LimitingElements_2 {

    static <E> Gatherer<E, ?, E> limit(long limit) {
        class Box {
            long counter = 0L;
        }
        Supplier<Box> initializer = Box::new;
        Gatherer.Integrator<Box, E, E> integrator = (box, element, downstream) -> {
            if (box.counter < limit) {
                box.counter++;
                return downstream.push(element);
            } else {
                return false;
            }
        };
        Gatherer<E, ?, E> gatherer = Gatherer.ofSequential(initializer, integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        var result = Stream.of(1, 2, 3, 4, 5)
                .gather(limit(3))
                .toList();
        System.out.println("result = " + result);
    }
}

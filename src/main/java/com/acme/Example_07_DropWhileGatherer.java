package com.acme;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_07_DropWhileGatherer {

    static <E> Gatherer<E, ?, E> dropWhile(Predicate<E> predicate) {
        class Gate {
            boolean open = false;
        }
        Supplier<Gate> initializer = Gate::new;
        Gatherer.Integrator<Gate, E, E> integrator = (gate, element, downstream) -> {
            if (!gate.open && predicate.test(element)) {
                gate.open = true;
            }
            if (gate.open) {
                downstream.push(element);
            }
            return true;  // not the right thing to do even though it works here...see article
        };
        Gatherer<E, ?, E> gatherer = Gatherer.ofSequential(initializer, integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        Predicate<Integer> predicate = x -> x > 3;
        var result = Stream.of(1, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1)
                .gather(dropWhile(predicate))
                .toList();
        System.out.println("result = " + result);
    }
}

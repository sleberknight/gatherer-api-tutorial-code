package com.acme;

import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_08_DropWhileGatherer_2 {

    static <E> Gatherer<E, ?, E> dropWhile(Predicate<E> predicate) {
        Gatherer<E, ?, E> gatherer = Gatherer.ofSequential(
                // using a non-denotable type as the initializer
                () -> new Object() {
                    boolean open = false;
                },
                (gate, element, downstream) -> {
                    if (!gate.open && predicate.test(element)) {
                        gate.open = true;
                    }
                    if (gate.open) {
                        downstream.push(element);
                    }
                    return true; // not the right thing to do even though it works here...see article
                });
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

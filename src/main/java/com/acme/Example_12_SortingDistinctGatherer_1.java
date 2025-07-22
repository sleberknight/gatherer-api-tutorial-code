package com.acme;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;

import java.util.Comparator;
import java.util.SequencedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_12_SortingDistinctGatherer_1 {

    static <E> Gatherer<E, ?, E> sort(Comparator<E> comparator) {
        
        Supplier<SequencedSet<E>> initializer = () -> new TreeSet<>(comparator);
        
        @SuppressWarnings("unused")
        Gatherer.Integrator<SequencedSet<E>, E, E> integrator = (set, element, downstream) -> {
            set.add(element);
            return true;
        };
        
        // The takeWhile is needed to avoid pushing elements that downstrean does not
        // want, for example when using a limit or findFirst.
        BiConsumer<SequencedSet<E>, Gatherer.Downstream<? super E>> finisher = 
                (set, downstream) -> set.stream()
                        .takeWhile(_ -> !downstream.isRejecting())
                        .forEach(downstream::push);

        // Instead of the takeWhile above, you can also do this:
        //
        // set.stream()
        //     .allMatch(downstream::push);
        //
        // This is because allMatch short-circuits on the first false value.
        
        Gatherer<E, SequencedSet<E>, E> gatherer = 
                Gatherer.ofSequential(initializer, integrator, finisher);
        
        return gatherer;
    }

    public static void main(String[] args) {
        Gatherer<String, ?, String> sort = sort(
                comparing(String::length).thenComparing(naturalOrder())
        );
        var result = Stream.of("one", "one", "two", "three", "three", "four", "five", "six", "five", "six", "seven")
                .gather(sort)
                .toList();
        System.out.println("result = " + result);
    }
}

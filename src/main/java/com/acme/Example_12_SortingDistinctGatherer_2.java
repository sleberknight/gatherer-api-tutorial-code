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

public class Example_12_SortingDistinctGatherer_2 {

    static <E> Gatherer<E, ?, E> sort(Comparator<E> comparator) {
        
        Supplier<SequencedSet<E>> initializer = () -> new TreeSet<>(comparator);
        
        @SuppressWarnings("unused")
        Gatherer.Integrator<SequencedSet<E>, E, E> integrator = (set, element, downstream) -> {
            set.add(element);
            return true;
        };

        BiConsumer<SequencedSet<E>, Gatherer.Downstream<? super E>> finisher = (set, downstream) -> {
            // Check if the downstream is rejecting before doing any work
            if (!downstream.isRejecting()) {
                set.stream().allMatch(downstream::push);
            }
        };
        
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

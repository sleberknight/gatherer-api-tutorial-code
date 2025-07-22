package com.acme;

import java.util.Comparator;
import java.util.SequencedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_13_ParallelSortingDistinctGatherer {

    static <E> Gatherer<E, ?, E> sort(Comparator<E> comparator) {
        Supplier<SequencedSet<E>> initializer = () -> new TreeSet<>(comparator);

        @SuppressWarnings("unused")
        Gatherer.Integrator<SequencedSet<E>, E, E> integrator = (set, element, downstream) -> {
            set.add(element);
            return true;
        };

        BinaryOperator<SequencedSet<E>> combiner = (set1, set2) -> {
            set1.addAll(set2);
            return set1;
        };

        BiConsumer<SequencedSet<E>, Gatherer.Downstream<? super E>> finisher = (set, downstream) -> {
            if (!downstream.isRejecting()) {
                set.stream().allMatch(downstream::push);
            }
        };

        Gatherer<E, SequencedSet<E>, E> gatherer = Gatherer.of(
                initializer, integrator, combiner, finisher);

        return gatherer;
    }

    public static void main(String[] args) {
        Gatherer<String, ?, String> sort = sort(
                Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
        var result = Stream.of("one", "one", "two", "three", "three", "four", "five", "six", "five", "six", "seven")
                .parallel()
                .gather(sort)
                .toList();
        System.out.println("result = " + result);
    }
}

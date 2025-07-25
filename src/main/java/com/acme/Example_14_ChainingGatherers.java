package com.acme;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_14_ChainingGatherers {

    static <E> Gatherer<E, ?, E> filtering(Predicate<E> filter) {
        Gatherer.Integrator<Void, E, E> integrator = (_, element, downstream) -> {
            if (filter.test(element)) {
                return downstream.push(element);
            }
            return true;
        };

        Gatherer<E, ?, E> gatherer = Gatherer.of(integrator);

        return gatherer;
    }

    static <E, R> Gatherer<E, ?, R> mapping(Function<E, R> mapper) {
        Gatherer.Integrator<Void, E, R> integrator = (_, element, downstream) -> 
                downstream.push(mapper.apply(element));

        Gatherer<E, ?, R> gatherer = Gatherer.of(integrator);

        return gatherer;
    }

    public static void main(String[] args) {
        Gatherer<String, ?, String> filter = filtering(s -> s.length() > 3);
        Gatherer<String, ?, String> mapper = mapping(String::toUpperCase);
        Gatherer<String, ?, String> filterThenMap = filter.andThen(mapper);
        var result = Stream.of("one", "two", "three", "four", "five", "six", "seven")
                .gather(filterThenMap)
                .toList();
        System.out.println("result = " + result);
    }
}

package com.acme;

import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_02_MappingGatherer {

    static <E, R> Gatherer<E, ?, R> mapping(Function<E, R> mapper) {
        Gatherer.Integrator<Void, E, R> integrator = (_, element, downstream) -> downstream.push(mapper.apply(element));
        Gatherer<E, ?, R> gatherer = Gatherer.of(integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        Function<String, String> mapper = String::toUpperCase;
        var mappingGatherer = mapping(mapper);
        var result = Stream.of("one", "two", "three")
                .gather(mappingGatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

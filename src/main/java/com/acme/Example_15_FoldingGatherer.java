package com.acme;

import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class Example_15_FoldingGatherer {

    public static void main(String[] args) {
        Gatherer<String, ?, String> fold = Gatherers.fold(
                () -> "{",
                (string, element) -> string + element
        );

        Gatherer<String, Void, String> finish = Gatherer.ofSequential(
                (_, element, downstream) -> {
                    element += "}";
                    return downstream.push(element);
                }
        );

        var result = Stream.of(1, 2, 3, 4)
                .map(Object::toString)
                .gather(fold.andThen(finish))
                .findFirst()
                .orElseThrow();

        System.out.println("result = " + result);
    }
}

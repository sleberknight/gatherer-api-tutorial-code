package com.acme;

import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_01_IdentityGatherer {
    public static void main(String[] args) {
        Gatherer.Integrator<Void, String, String> integrator = (_, element, downstream) -> downstream.push(element);
        Gatherer<String, ?, String> identityGatherer = Gatherer.of(integrator);

        var result = Stream.of("one", "two", "three")
                .gather(identityGatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

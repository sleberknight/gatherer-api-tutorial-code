package com.acme;

import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class Example_16_ScanningGatherer {

    public static void main(String[] args) {
        Gatherer<String, ?, String> scan = Gatherers.scan(
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
                .gather(scan.andThen(finish))
                .toList();
        
        System.out.println("result = " + result);
    }
}

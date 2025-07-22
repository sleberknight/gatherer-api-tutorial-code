package com.acme;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_11_FinisherWithNoState {

    static <E, R> Gatherer<E, ?, R> mapThenFinish(Function<E, R> mapper, Supplier<R> supplier) {
        Gatherer.Integrator<Void, E, R> integrator = (_, element, downstream) -> 
                downstream.push(mapper.apply(element));
        BiConsumer<Void, Gatherer.Downstream<? super R>> finisher = (_, downstream) -> 
                downstream.push(supplier.get());
        Gatherer<E, ?, R> gatherer = Gatherer.of(integrator, finisher);
        return gatherer;
    }

    public static void main(String[] args) {
        Gatherer<String, ?, String> gatherer = mapThenFinish(String::toUpperCase, () -> "DONE");
        var result = Stream.of("one", "two", "three")
                .gather(gatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

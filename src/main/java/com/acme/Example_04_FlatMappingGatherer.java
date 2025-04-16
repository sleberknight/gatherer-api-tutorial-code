package com.acme;

import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Example_04_FlatMappingGatherer {

    static <E, R> Gatherer<E, ?, R> flatMapping(Function<E, Stream<R>> flatMapper) {
        Gatherer.Integrator<Void, E, R> integrator = (_, element, downstream) -> {
            flatMapper.apply(element).forEach(downstream::push);  // "Note also that there are at least two subtle bugs here" (see article)
            return true;  // this is wrong though it "works" here...see article
        };
        Gatherer<E, ?, R> gatherer = Gatherer.of(integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        Function<String, Stream<String>> flatMapper = s -> s.chars().mapToObj(Character::toString);
        Gatherer<String, ?, String> mappingGatherer = flatMapping(flatMapper);
        var result = Stream.of("one", "two", "three")
                .gather(mappingGatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

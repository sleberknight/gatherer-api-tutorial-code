package com.acme;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/**
 * Fusing Mappings, Filterings and Flat-Mappings
 */
public class Example_06_FusingGatherer_Fixed {

    static <E, R, RR> Gatherer<E, ?, RR> fusing(
            Function<E, R> mapper,
            Predicate<R> filter,
            Function<R, Stream<RR>> flatMapper) {

        Gatherer.Integrator<Void, E, RR> integrator = (_, element, downstream) -> {
            var mapped = mapper.apply(element);
            if (filter.test(mapped)) {
                // Use try-with-resources to ensure the stream is closed
                try (var flatMapped = flatMapper.apply(mapped)) {
                    flatMapped.sequential().forEach(downstream::push);  // Add sequential() to protect against parallel streams
                }
            }
            return true;  // still not correct...see article
        };
        Gatherer<E, ?, RR> gatherer = Gatherer.of(integrator);
        return gatherer;
    }

    public static void main(String[] args) {
        Function<String, String> mapper = String::toUpperCase;
        Predicate<String> filter = s -> s.length() == 3;
        Function<String, Stream<String>> flatMapper = s -> s.chars().mapToObj(Character::toString);
        Gatherer<String, ?, String> fusedGatherer = fusing(mapper, filter, flatMapper);
        var result = Stream.of("one", "two", "three")
                .gather(fusedGatherer)
                .toList();
        System.out.println("result = " + result);
    }
}

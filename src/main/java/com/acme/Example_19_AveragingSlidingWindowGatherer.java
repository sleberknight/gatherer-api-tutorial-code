package com.acme;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class Example_19_AveragingSlidingWindowGatherer {

    public static void main(String[] args) {
        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // var numbers = List.of(1, 2);
        // var numbers = List.<Integer>of();

        Gatherer<Integer, ?, List<Integer>> createSlidingWindows = Gatherers.windowSliding(3);
        
        Gatherer<List<Integer>, ?, Double> averagingDouble = Gatherer.of(
                (_, element, downstream) -> {
                    if (downstream.isRejecting()) {
                        return false;
                    } 
                    
                    // element is a List<Integer> (the window)
                    var optAverage = element.stream()
                            .mapToInt(i -> i)
                            .average();
                    
                    if (optAverage.isPresent()) {
                        return downstream.push(optAverage.getAsDouble());
                    } else {
                        return !downstream.isRejecting();
                    }
                });

        var result = numbers.stream()
                .gather(createSlidingWindows.andThen(averagingDouble))
                .toList();

        System.out.println("result = " + result);
    }
}

package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;
import java.util.List;
import java.util.stream.Stream;

public interface StatsTemplate {

    boolean filter(CarIF car);

    default Stream<CarIF> toFilteredStream(List<CarIF> cars) {
        return cars.stream()
                .filter(this::filter);
    }

    /**
     * @param cars List of cars
     * @return Sum of all ticket prices in cents
     */
    default long getPriceSum(List<CarIF> cars) {
        return toFilteredStream(cars)
                .mapToLong(c -> c.getTicket().getPrice())
                .sum();
    }

    /**
     * @param cars List of cars
     * @return Average of all ticket prices in cents
     */
    default int getPriceAvg(List<CarIF> cars) {
        return (int) Math.round(
                toFilteredStream(cars)
                .mapToLong(c -> c.getTicket().getPrice())
                .average().orElse(0.0)
        );
    }

    /**
     * @param cars List of cars
     * @return Average of all ticket durations in milliseconds
     */
    default int getDurationAvg(List<CarIF> cars) {
        return (int) Math.round(
                toFilteredStream(cars)
                .mapToLong(c -> c.getTicket().getDuration())
                .average().orElse(0.0)
        );
    }

    /**
     * @param cars List of cars
     * @return Count of cars
     */
    default int getTransctions(List<CarIF> cars) {
        return (int) toFilteredStream(cars)
                .count();
    }

}

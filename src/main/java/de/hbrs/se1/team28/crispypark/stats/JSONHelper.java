package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.List;

public class JSONHelper {

    private JSONHelper() {
        // prevent instantiation
    }

    /**
     * @param cars List of cars
     * @return Array of car no
     */
    public static JsonArray toCarNumberArray(List<CarIF> cars) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (CarIF c: cars) {
            builder.add(c.getNo());
        }
        return builder.build();
    }

    /**
     * @param cars List of cars
     * @return Duration in hours
     */
    public static JsonArray toCarDurationArray(List<CarIF> cars) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (CarIF c: cars) {
            builder.add(c.getTicket().getDuration() / (1000.0 * 60.0 * 60.0));
        }
        return builder.build();
    }

    /**
     * @param cars List of cars
     * @return Count of DriverTypes
     */
    public static JsonArray toCarCategoryArray(List<CarIF> cars) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        builder.add( cars.stream()
                .filter(c -> c.getType() == DriverType.NORMAL)
                .count()
        );
        builder.add( cars.stream()
                .filter(c -> c.getType() == DriverType.WOMEN)
                .count()
        );
        builder.add( cars.stream()
                .filter(c -> c.getType() == DriverType.DISABLED)
                .count()
        );
        return builder.build();
    }

    /**
     * @param cars List of cars
     * @return Count of TicketTypes
     */
    public static JsonArray toTicketTypeArray(List<CarIF> cars) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        builder.add( cars.stream()
                .filter(c -> c.getTicket().getTicketType() == TicketType.NORMAL)
                .count()
        );
        builder.add( cars.stream()
                .filter(c -> c.getTicket().getTicketType() == TicketType.FAMILY)
                .count()
        );
        builder.add( cars.stream()
                .filter(c -> c.getTicket().getTicketType() == TicketType.MEMBER)
                .count()
        );
        builder.add( cars.stream()
                .filter(c -> c.getTicket().getTicketType() == TicketType.MONTHLY)
                .count()
        );
        return builder.build();
    }

}

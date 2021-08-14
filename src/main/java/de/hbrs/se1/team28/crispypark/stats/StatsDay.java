package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;
import java.time.LocalDate;

public class StatsDay implements StatsTemplate {

    private final LocalDate DATE;

    public StatsDay(LocalDate day) {
        this.DATE = day;
    }

    @Override
    public boolean filter(CarIF car) {
        return DATE.isEqual( car.getTicket().getLeaveTime().toLocalDate() );
    }
}

package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;
import java.time.LocalDate;

public class StatsMonth implements StatsTemplate {

    private final LocalDate DATE;

    public StatsMonth(LocalDate month) {
        this.DATE = month;
    }

    @Override
    public boolean filter(CarIF car) {
        final LocalDate TICKET = car.getTicket().getLeaveTime().toLocalDate();
        return ( DATE.getYear() == TICKET.getYear() )
                && ( DATE.getMonth() == TICKET.getMonth() );
    }
}

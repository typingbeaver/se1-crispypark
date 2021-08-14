package de.hbrs.se1.team28.crispypark;

import java.time.ZonedDateTime;

public interface TicketIF {

    String getID();
    TicketType getTicketType();

    ZonedDateTime getEntranceTime();
    ZonedDateTime getLeaveTime();

    void leave();
    void leave(long duration);
    long getDuration();
    long getDurationSinceEntrance();

    int getPrice();
}

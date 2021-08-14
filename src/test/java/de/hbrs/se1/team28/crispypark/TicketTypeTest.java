package de.hbrs.se1.team28.crispypark;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTypeTest {

    @Test
    @DisplayName("Ticket rate can be changed.")
    public void testRateChange() {
        final int NEW_RATE = 20;
        TicketType.NORMAL.setRate(NEW_RATE);
        assertEquals(NEW_RATE, TicketType.NORMAL.getRate());
        TicketType.NORMAL.setRate(-60);
        assertEquals(NEW_RATE, TicketType.NORMAL.getRate());
    }
}

package de.hbrs.se1.team28.crispypark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TicketIFTest {

    Ticket t, t2;

    @BeforeEach
    void setUp() {
         t = Ticket.create(0, TicketType.NORMAL);
         t2 = Ticket.create(0, TicketType.FAMILY);
    }

    @Test
    @DisplayName("Ticket creator creates correct type of tickets.")
    void testTicketCreator() {
        assertTrue(t instanceof SingleTicket);
        assertTrue(t2 instanceof SingleTicket);
        assertTrue(Ticket.create(0, TicketType.MEMBER) instanceof SingleTicket);
        assertTrue(Ticket.create(0, TicketType.MONTHLY) instanceof TimeTicket);
    }

    @Test
    @DisplayName("Ticket can be used with real time data.")
    void testTicketRealTime() throws InterruptedException {
        final int DELTA = 20;   // ms

        // Entrance time
        final long NOW = Instant.now().toEpochMilli();
        assertTrue(Math.abs(NOW - t.getEntranceTime().toInstant().toEpochMilli()) < DELTA);

        // Duration and leave time
        final int SLEEP = 700;  // ms
        TimeUnit.MILLISECONDS.sleep(SLEEP);
        assertNull(t.getLeaveTime());   // not left yet
        t.leave();
        assertTrue(Math.abs(SLEEP - t.getDuration()) < DELTA);
        assertTrue( Math.abs(NOW + SLEEP - t.getLeaveTime().toInstant().toEpochMilli()) < DELTA);
    }

    @Test
    @DisplayName("Ticket can be used with given time data.")
    void testTicketGivenTime() {
        // Entrance time
        final long NOW = Instant.now().toEpochMilli();
        final Ticket T = Ticket.create(0, TicketType.MEMBER, NOW);
        assertEquals(NOW, T.getEntranceTime().toInstant().toEpochMilli());

        // Duration and leave time
        // Negative durations do not work
        T.leave(-1000);
        assertEquals(-1, T.getDuration());
        assertNull(t.getLeaveTime());   // not left yet
        // Real duration
        final int DURATION = 12000;  //ms
        T.leave(DURATION);
        assertEquals(DURATION, T.getDuration());
        assertEquals(NOW + DURATION, T.getLeaveTime().toInstant().toEpochMilli());
    }

    @Test
    @DisplayName("Ticket ID is a unique 16 character string.")
    void testTicketID() {
        assertNotNull(t.getID());
        assertEquals(16,t.getID().length());
        assertNotEquals(t.getID(), t2.getID());
    }

    @Test
    @DisplayName("Ticket has a specified type.")
    void testGetTicketType() {
        assertEquals(t.getTicketType(), TicketType.NORMAL);
        assertEquals(t2.getTicketType(), TicketType.FAMILY);
        assertEquals(Ticket.create(0, TicketType.MEMBER).getTicketType(), TicketType.MEMBER);
        assertEquals(Ticket.create(0, TicketType.MONTHLY).getTicketType(), TicketType.MONTHLY);
    }

    @Test
    @DisplayName("Ticket returns duration since entrance or after leave.")
    void testDuration() throws InterruptedException {
        // Duration before leave
        TimeUnit.MILLISECONDS.sleep(5);
        final long DUR1 = t.getDurationSinceEntrance();
        TimeUnit.MILLISECONDS.sleep(50);
        final long DUR2 = t.getDurationSinceEntrance();
        assertTrue(DUR1 > 0);
        assertTrue(DUR1 < DUR2);
        assertEquals(-1, t.getDuration());

        // duration after Leave
        final int DURATION = 200;
        t.leave(DURATION);
        assertEquals(DURATION, t.getDuration());
        TimeUnit.MILLISECONDS.sleep(100);
        assertEquals(DURATION, t.getDuration());
        assertEquals(DURATION, t.getDurationSinceEntrance());
    }

    @Test
    @DisplayName("Price gets calculated correctly (per 1 hour).")
    void testSinglePrice() throws InterruptedException {
        Ticket normal = Ticket.create(0, TicketType.NORMAL);
        // price calculation while parking
        TimeUnit.MILLISECONDS.sleep(50);
        assertEquals(TicketType.NORMAL.getRate(), normal.getPrice());
        // after leave
        normal.leave(60 * 60 * 1000); // 1 hour
        assertEquals(TicketType.NORMAL.getRate(), normal.getPrice());

        Ticket member = Ticket.create(0, TicketType.MEMBER);
        member.leave(3 * 60 * 60 * 1000); // 3 hours
        assertEquals(TicketType.MEMBER.getRate()*3, member.getPrice());

        Ticket family = Ticket.create(0, TicketType.FAMILY);
        family.leave(9 * 60 * 60 * 1000 + 59 * 60 * 1000); // 9:59 hours
        assertEquals(TicketType.FAMILY.getRate()*10, family.getPrice());
    }

    @Test
    @DisplayName("Time tickets are only charged first time.")
    void testTimePrice() {
        // first parking session
        // MAKE SURE NO ISN'T USED ALREADY
        Ticket monthly = Ticket.create(1, TicketType.MONTHLY);
        monthly.leave(30 * 60 * 1000); // 30 minutes
        assertEquals(TicketType.MONTHLY.getRate(), monthly.getPrice());

        // following parking sessions
        monthly = Ticket.create(1, TicketType.MONTHLY);
        monthly.leave(3 * 60 * 60 * 1000); // 3 hours
        assertEquals(0, monthly.getPrice());

        // expiration after 30 days
        monthly = Ticket.create(1, TicketType.MONTHLY, Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli());
        monthly.leave(2 * 60 * 60 * 1000); // 2 hours
        assertEquals(TicketType.MONTHLY.getRate(), monthly.getPrice());
    }

}
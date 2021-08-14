package de.hbrs.se1.team28.crispypark;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CarIFTest {
    // event,Nr,Timer,Duration,Price,Hash,Color,Space,client_category,vehicle_type,license,begin
    final String[] PARAMS = new String[]{"", "1", "", "", "", "", "#cd1db6", "1", "NORMAL", "NORMAL", "TE-ST 1"};
    final Car C = new Car(PARAMS);

    @Test
    @DisplayName("Car has given attributes.")
    void testCarNo() {
        assertEquals(Integer.parseInt(PARAMS[1]), C.getNo());
        assertEquals(PARAMS[10], C.getLicense());
        assertEquals(DriverType.NORMAL, C.getType());
    }

    @Test
    @DisplayName("Car has a ticket.")
    void testTicket() {
        assertNotNull(C.getTicket());
    }

    @Test
    @DisplayName("Car gets colored by TicketType.")
    void testColor() {
        assertEquals(C.getTicket().getTicketType().getColor(), C.getColor());
    }

    @Test
    @DisplayName("Car's space can be set, default before location is -1.")
    void testCarSpace() {
        assertEquals(-1, C.getSpace());
        C.setSpace(5);
        assertEquals(5, C.getSpace());
    }

}

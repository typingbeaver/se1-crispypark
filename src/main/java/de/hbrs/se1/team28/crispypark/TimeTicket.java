package de.hbrs.se1.team28.crispypark;

import java.time.LocalDate;
import java.util.HashMap;

public class TimeTicket extends Ticket {

    private static final HashMap<Integer, LocalDate> TIME_TICKETS = new HashMap<>();

    public TimeTicket(int no, TicketType type, long entranceTime) {
        super(type, entranceTime);

        // Set parking rate
        // Check validity
        LocalDate purchaseDate = TIME_TICKETS.get(no);
        if ((purchaseDate != null) && (ENTRANCE.toLocalDate().toEpochDay() - purchaseDate.toEpochDay()) < 30) {
            // Time ticket valid
            super.parkingRate = 0;

        } else {
            // New time ticket
            TIME_TICKETS.put(no, this.ENTRANCE.toLocalDate());
            super.parkingRate = this.TYPE.getRate();    // prevents price change during parking session
        }
    }

    @Override
    public int getPrice() {
        // time tickets have fixed price
        return parkingRate;
    }
}

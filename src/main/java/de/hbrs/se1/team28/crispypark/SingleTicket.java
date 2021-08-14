package de.hbrs.se1.team28.crispypark;

public class SingleTicket extends Ticket {

    public SingleTicket(TicketType type, long entranceTime) {
        super(type, entranceTime);

        // Set parking rate
        super.parkingRate = TYPE.getRate(); // prevents price change during parking session
    }

    @Override
    public int getPrice() {
        // Time dependant price
        final int PERIOD = 60 * 60 * 1000;    // 1 hour
        final long DURATION = (this.duration < 0) ? this.getDurationSinceEntrance() : this.duration;
        final int TIMES = (DURATION % PERIOD != 0)
                ? ((int) (DURATION / PERIOD) + 1)  // charge every begun period
                : ((int) (DURATION / PERIOD));

        return parkingRate * TIMES;
    }
}

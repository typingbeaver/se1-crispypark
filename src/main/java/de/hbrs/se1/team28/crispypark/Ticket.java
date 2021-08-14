package de.hbrs.se1.team28.crispypark;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public abstract class Ticket implements TicketIF {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 16;
    private static final ZoneId TIME_ZONE = ZoneId.of("CET");
    
    private final String TICKET_ID;
    protected final ZonedDateTime ENTRANCE;
    protected final TicketType TYPE;
    protected int parkingRate;
    protected long duration = -1;

    /*
     * Factory Method "light"
     * Constructs sub-class of Ticket depending on given type
     */
    public static Ticket create(int no, TicketType type, long entranceTime) {
        if (type == TicketType.MONTHLY) {
            return new TimeTicket(no, type, entranceTime);
        }
        return new SingleTicket(type, entranceTime);
    }

    public static Ticket create(int no, TicketType type) {
        return create(no, type, Instant.now().toEpochMilli());
    }

    protected Ticket(TicketType type, long entranceTime) {
        // Set ticketID
        Random rand = new Random();
        char[] id = new char[LENGTH];

        for(int i = 0; i < LENGTH; i++) {
            id[i] = CHARACTERS.charAt(rand.nextInt(CHARACTERS.length()));
        }

        this.TICKET_ID = new String(id);

        // Set Entrance Date
        this.ENTRANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(entranceTime), TIME_ZONE);

        // Set Ticket Type
        this.TYPE = type;
    }

    @Override
    public String getID() {
        return TICKET_ID;
    }

    @Override
    public TicketType getTicketType() {
        return TYPE;
    }

    @Override
    public ZonedDateTime getEntranceTime() {
        return ENTRANCE;
    }

    @Override
    public ZonedDateTime getLeaveTime() {
        return (this.duration >= 0) ? ENTRANCE.plus(this.duration, ChronoUnit.MILLIS) : null;
    }

    @Override
    public void leave() {
        this.duration = this.getDurationSinceEntrance();
    }

    @Override
    public void leave(long duration) {
        this.duration = (duration >= 0) ? duration : -1;
    }

    @Override
    public long getDuration() {
        return (this.duration > 0) ? this.duration : -1;
    }

    @Override
    public long getDurationSinceEntrance() {
        return (this.duration > 0)
                ? this.duration
                : Duration.between(ENTRANCE, LocalDateTime.now().atZone(TIME_ZONE)).toMillis();
    }

    /**
     * Calculates parking price
     * @return total price in cents
     */
    @Override
    public abstract int getPrice();

}

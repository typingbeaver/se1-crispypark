package de.hbrs.se1.team28.crispypark;

public class Car implements CarIF {
    final int NO;
    final String LICENSE;
    final String COLOR;
    final DriverType TYPE;
    final TicketIF TICKET;
    int space;

    public Car( String[] params){
        this.NO = Integer.parseInt(params[1]);
        this.LICENSE = params[10];
        this.space = -1;
        this.TYPE = DriverType.valueOf(params[9]);

        final TicketType TICKET_TYPE = TicketType.valueOf(params[8]);
        TicketIF ticket;
        try {
            // time given by ccm.parkhaus
            ticket = Ticket.create(this.NO, TICKET_TYPE, Integer.parseInt(params[2]));
        } catch (Exception e) {
            // use realtime data instead
            ticket = Ticket.create(this.NO, TICKET_TYPE);
        }
        this.TICKET = ticket;
        this.COLOR = TICKET.getTicketType().getColor();
    }

    @Override
    public int getNo() {
        return this.NO;
    }

    @Override
    public String getLicense() {
        return this.LICENSE;
    }

    @Override
    public DriverType getType() {
        return this.TYPE;
    }

    @Override
    public TicketIF getTicket() {
        return this.TICKET;
    }

    @Override
    public String getColor() {
        return COLOR;
    }

    @Override
    public int getSpace() {
        return this.space;
    }

    @Override
    public void setSpace(int space) {
        this.space = space;
    }

    @Override
    public String toString(){
        return this.NO +
                "/" +
                this.TICKET.getEntranceTime().toInstant().toEpochMilli() +
                "/" +
                (this.TICKET.getDuration() >= 0 ? this.TICKET.getDuration() : "_") +
                "/" +
                (this.TICKET.getDuration() >= 0 ? this.TICKET.getPrice() : "_") +
                "/" +
                this.TICKET.getID() +
                "/" +
                this.COLOR +
                "/" +
                this.space +
                "/" +
                this.TICKET.getTicketType().toString() +
                "/" +
                this.TYPE.toString() +
                "/" +
                this.LICENSE;
    }
}

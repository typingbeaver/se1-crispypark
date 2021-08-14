package de.hbrs.se1.team28.crispypark;

public interface CarIF {
    int getNo();
    String getLicense();
    DriverType getType();
    TicketIF getTicket();
    String getColor();
    int getSpace();
    void setSpace(int s);
}

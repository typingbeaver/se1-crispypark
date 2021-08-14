package de.hbrs.se1.team28.crispypark;

public enum TicketType {
    NORMAL (100, "#212529"),    // dark grey
    FAMILY (90, "#198754"),     // green
    MEMBER (85, "#dc3545"),     // red
    MONTHLY (4000, "#ffc107");  // yellow

    private int rate;
    private final String COLOR;

    TicketType(int rate, String color) {
        this.rate = rate;
        this.COLOR = color;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        if (rate >= 0) {
            this.rate = rate;
        }
    }

    public String getColor() {
        return this.COLOR;
    }

}



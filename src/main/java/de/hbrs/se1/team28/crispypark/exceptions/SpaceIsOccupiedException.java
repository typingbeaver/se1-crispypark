package de.hbrs.se1.team28.crispypark.exceptions;

public class SpaceIsOccupiedException extends Exception {
    public SpaceIsOccupiedException(int no) {
        super("Space " + no + " is already occupied.");
    }
}

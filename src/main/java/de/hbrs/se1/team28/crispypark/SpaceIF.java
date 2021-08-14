package de.hbrs.se1.team28.crispypark;

import de.hbrs.se1.team28.crispypark.exceptions.SpaceIsOccupiedException;

public interface SpaceIF {

    int getNo();

    DriverType getType();
    void setType(DriverType type);

    CarIF getCar();
    void setCar(CarIF car) throws SpaceIsOccupiedException;
    CarIF removeCar();
}

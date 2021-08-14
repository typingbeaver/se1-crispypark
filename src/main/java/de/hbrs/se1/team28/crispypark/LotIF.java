package de.hbrs.se1.team28.crispypark;

import java.util.List;

public interface LotIF {

    int getCount();

    SpaceIF getSpace(int number);
    void addSpace();
    void removeSpace();
    void setSpaceTypes(int[] amount);

    SpaceIF getFreeSpace(DriverType type);
    CarIF unparkCar(int carNo);
    List<CarIF> getCars();
    String getSpaceColoring();
}

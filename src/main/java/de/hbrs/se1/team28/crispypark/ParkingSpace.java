package de.hbrs.se1.team28.crispypark;

import de.hbrs.se1.team28.crispypark.exceptions.SpaceIsOccupiedException;

public class ParkingSpace implements SpaceIF {

    private final int NO;
    private DriverType type;
    private CarIF car;

    public ParkingSpace(int no) {
        this.NO = no;
        this.type = DriverType.NORMAL;
    }

    @Override
    public int getNo() {
        return this.NO;
    }

    @Override
    public DriverType getType() {
        return this.type;
    }

    @Override
    public void setType(DriverType type) {
        this.type = type;
    }

    @Override
    public CarIF getCar() {
        return this.car;
    }

    @Override
    public void setCar(CarIF car) throws SpaceIsOccupiedException {
        if (this.car == null) {
            this.car = car;
        } else {
            throw new SpaceIsOccupiedException(this.NO);
        }
    }

    @Override
    public CarIF removeCar() {
        final CarIF CAR = this.car;
        this.car = null;
        return CAR;
    }

}


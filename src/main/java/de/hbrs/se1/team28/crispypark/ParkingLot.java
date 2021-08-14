package de.hbrs.se1.team28.crispypark;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParkingLot implements LotIF {

    private final List<SpaceIF> SPACES;
    private int count = 0;

    public ParkingLot(int number) {
        SPACES = new ArrayList<>();
        while (count < number) {
            addSpace();
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public SpaceIF getSpace(int number) {
        return SPACES.get(number - 1);
    }

    @Override
    public void addSpace() {
        SPACES.add( new ParkingSpace(++count) );
    }

    @Override
    public void removeSpace() {
        if ( SPACES.get(count - 1).getCar() == null ) {
            SPACES.remove(--count);
        }
    }

    /**
     * Sets spaces to specific DriverType
     * @param amount [DISABLED, WOMEN]
     */
    @Override
    public void setSpaceTypes(int[] amount) {
        final int DISABLED = amount[0];
        final int WOMEN = amount[1];

        try {
            for (int d = 0; d < DISABLED; d++) {
                SPACES.get(d).setType(DriverType.DISABLED);
            }
            for (int w = DISABLED; w < (WOMEN + DISABLED); w++) {
                SPACES.get(w).setType(DriverType.WOMEN);
            }
            for (int n = (DISABLED + WOMEN); n < count; n++) {
                SPACES.get(n).setType(DriverType.NORMAL);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignored
        }
    }

    @Override
    public SpaceIF getFreeSpace(DriverType type) {
        return SPACES.stream()
                .filter(s -> s.getType() == type)
                .filter(s -> s.getCar() == null)
                .findAny().orElse(null);
    }

    @Override
    public CarIF unparkCar(int carNo) {
        return SPACES.stream()
                .filter(s -> s.getCar() != null)
                .filter(s -> s.getCar().getNo() == carNo)
                .map(SpaceIF::removeCar)
                .findFirst().orElse(null);
    }

    @Override
    public List<CarIF> getCars() {
        return SPACES.stream()
                .map(SpaceIF::getCar)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String getSpaceColoring() {
        final JsonObjectBuilder COLORING = Json.createObjectBuilder();
        for (SpaceIF s : SPACES) {
            COLORING.add(Integer.toString(s.getNo()), s.getType().getColor());
        }
        return COLORING.build().toString();
    }
}

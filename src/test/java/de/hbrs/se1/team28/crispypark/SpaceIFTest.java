package de.hbrs.se1.team28.crispypark;

import de.hbrs.se1.team28.crispypark.exceptions.SpaceIsOccupiedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceIFTest {

    static SpaceIF space;
    static CarIF car;

    @BeforeAll
    static void setUp() {
        space = new ParkingSpace(1);
        final String[] PARAMS = new String[]{"", "1", "", "", "", "", "#cd1db6", "1", "NORMAL", "WOMEN", "TE-ST 1"};
        car = new Car(PARAMS);
    }

    @Test
    @DisplayName("Space no. can be retrieved.")
    void testNo() {
        assertEquals(1, space.getNo());
    }

    @Test
    @DisplayName("Space type can be changed.")
    void testType() {
        assertEquals(DriverType.NORMAL, space.getType());
        space.setType(DriverType.WOMEN);
        assertEquals(DriverType.WOMEN, space.getType());
    }

    @Test
    @DisplayName("Space can be occupied and freed by a car.")
    void testOccupation() throws SpaceIsOccupiedException {
        // Space empty
        assertNull(space.getCar());

        // park car
        space.setCar(car);
        assertEquals(car, space.getCar());

        // space occupied
        assertThrows(SpaceIsOccupiedException.class, () -> space.setCar(car) );

        // space can be freed
        space.removeCar();
        assertNull(space.getCar());
    }

}

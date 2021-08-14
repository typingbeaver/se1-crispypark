package de.hbrs.se1.team28.crispypark;

import de.hbrs.se1.team28.crispypark.exceptions.SpaceIsOccupiedException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LotIFTest {

    static LotIF lot;
    static CarIF car;

    @BeforeAll
    static void setUp() {
        lot = new ParkingLot(5);
        final String[] PARAMS = new String[]{"", "1", "", "", "", "", "#cd1db6", "1", "NORMAL", "WOMEN", "TE-ST 1"};
        car = new Car(PARAMS);
    }

    @Test
    @Order(1)
    @DisplayName("Lot size can be increased.")
    void testAddSpace() {
        final int COUNT = lot.getCount();
        lot.addSpace();
        assertEquals(COUNT + 1, lot.getCount());
    }

    @Test
    @Order(2)
    @DisplayName("Lot size can be decreased if not occupied.")
    void testRemoveSpace() throws SpaceIsOccupiedException {
        // Space occupied
        final int COUNT = lot.getCount();
        lot.getSpace(COUNT).setCar(car);
        lot.removeSpace();
        assertEquals(COUNT, lot.getCount());

        // Space empty
        lot.getSpace(COUNT).removeCar();
        lot.removeSpace();
        assertEquals(COUNT - 1, lot.getCount());
    }

    @Test
    @Order(3)
    @DisplayName("Space types can be changed.")
    void testSpaceTypes() {
        final int[] TYPES = {2,1};
        lot.setSpaceTypes(TYPES);
        assertEquals(DriverType.DISABLED, lot.getSpace(1).getType());
        assertEquals(DriverType.DISABLED, lot.getSpace(2).getType());
        assertEquals(DriverType.WOMEN,    lot.getSpace(3).getType());
        assertEquals(DriverType.NORMAL,   lot.getSpace(4).getType());
    }

    @Test
    @Order(4)
    @DisplayName("Search for free parking spaces with specified type")
    void testFreeSpace() throws SpaceIsOccupiedException {
        SpaceIF space = lot.getFreeSpace(DriverType.WOMEN);
        assertEquals(DriverType.WOMEN, space.getType());
        assertEquals(3, space.getNo());

        // block space
        space.setCar(car);
        space = lot.getFreeSpace(DriverType.WOMEN);
        assertNull(space);
    }

    @Test
    @Order(5)
    @DisplayName("List of all parking cars can be retrieved.")
    void testCarsList() {
        List<CarIF> cars = new ArrayList<>();
        cars.add(car);
        assertEquals(cars, lot.getCars());
    }

    @Test
    @Order(6)
    @DisplayName("Car can be unparked by its number.")
    void testUnpark() {
        CarIF unparked = lot.unparkCar(car.getNo());
        assertEquals(car, unparked);
        assertNull(lot.getSpace(3).getCar());

        // mon existing car
        assertNull( lot.unparkCar(99) );
    }

}

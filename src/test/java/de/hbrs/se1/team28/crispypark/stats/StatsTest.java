package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsTest {

    static List<CarIF> cars = new ArrayList<>();
    static int PRICE;
    static int AVERAGE;
    static int DURATION;
    static int COUNT;

    @BeforeAll
    public static void setup() throws InterruptedException {
        Car c1 = new Car( new String[]{"", "1", "", "", "", "", "#000000", "1", "NORMAL", "NORMAL", "TE-ST 1"} );
        cars.add(c1);
        Car c2 = new Car( new String[]{"", "2", "", "", "", "", "#ffffff", "2", "NORMAL", "NORMAL", "TE-ST 2"} );
        cars.add(c2);

        c1.getTicket().leave(1000);
        c2.getTicket().leave(500);

        PRICE = 2 * TicketType.NORMAL.getRate();
        AVERAGE = PRICE / 2;
        DURATION = (1000 + 500) / 2;
        COUNT = 2;
    }

    @Test
    public void testStatsAll() {
        final StatsAll ALL = new StatsAll();
        assertEquals(PRICE, ALL.getPriceSum(cars));
        assertEquals(AVERAGE, ALL.getPriceAvg(cars));
    }

    @Test
    public void testStatsDay() {
        final StatsDay TODAY = new StatsDay(LocalDate.now());
        final StatsDay YESTERDAY = new StatsDay(LocalDate.now().minusDays(1));

        assertEquals(PRICE, TODAY.getPriceSum(cars));
        assertEquals(AVERAGE, TODAY.getPriceAvg(cars));
        assertEquals(DURATION, TODAY.getDurationAvg(cars));
        assertEquals(COUNT, TODAY.getTransctions(cars));

        assertEquals(0, YESTERDAY.getPriceSum(cars));
        assertEquals(0, YESTERDAY.getPriceAvg(cars));
        assertEquals(0, YESTERDAY.getDurationAvg(cars));
        assertEquals(0, YESTERDAY.getTransctions(cars));
    }

    @Test
    public void testStatsMonth() {
        final StatsMonth TODAY = new StatsMonth(LocalDate.now());
        final StatsMonth LAST_MONTH = new StatsMonth(LocalDate.now().minusMonths(1));
        final StatsMonth THIS_MONTH_LAST_YEAR = new StatsMonth(LocalDate.now().minusYears(1));

        assertEquals(PRICE, TODAY.getPriceSum(cars));
        assertEquals(AVERAGE, TODAY.getPriceAvg(cars));
        assertEquals(DURATION, TODAY.getDurationAvg(cars));
        assertEquals(COUNT, TODAY.getTransctions(cars));

        assertEquals(0, LAST_MONTH.getPriceSum(cars));
        assertEquals(0, LAST_MONTH.getPriceAvg(cars));
        assertEquals(0, LAST_MONTH.getDurationAvg(cars));
        assertEquals(0, LAST_MONTH.getTransctions(cars));

        assertEquals(0, THIS_MONTH_LAST_YEAR.getPriceSum(cars));
        assertEquals(0, THIS_MONTH_LAST_YEAR.getPriceAvg(cars));
        assertEquals(0, THIS_MONTH_LAST_YEAR.getDurationAvg(cars));
        assertEquals(0, THIS_MONTH_LAST_YEAR.getTransctions(cars));
    }

    @Test
    public void testStatsYear() {
        final StatsYear TODAY = new StatsYear(LocalDate.now());
        final StatsYear LAST_YEAR = new StatsYear(LocalDate.now().minusYears(1));

        assertEquals(PRICE, TODAY.getPriceSum(cars));
        assertEquals(AVERAGE, TODAY.getPriceAvg(cars));
        assertEquals(DURATION, TODAY.getDurationAvg(cars));
        assertEquals(COUNT, TODAY.getTransctions(cars));

        assertEquals(0, LAST_YEAR.getPriceSum(cars));
        assertEquals(0, LAST_YEAR.getPriceAvg(cars));
        assertEquals(0, LAST_YEAR.getDurationAvg(cars));
        assertEquals(0, LAST_YEAR.getTransctions(cars));
    }

}

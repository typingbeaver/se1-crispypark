package de.hbrs.se1.team28.crispypark.stats;

import de.hbrs.se1.team28.crispypark.*;

public class StatsAll implements StatsTemplate {
    @Override
    public boolean filter(CarIF car) {
        return true;
    }
}

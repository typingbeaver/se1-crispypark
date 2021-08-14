package de.hbrs.se1.team28.crispypark.servlets;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CrispyParkServlet", value = "")
public class CrispyParkServlet extends ParkhausServlet {

    private int max = 60;   // no of spaces
    private int open = 0;   // opening time
    private int close = 24; // closing time
    private int[] special = new int[]{16, 8};  // disabled, women spaces

    @Override
    String NAME(){
        return "CrispyPark";
    }

    @Override
    int max(){
        return this.max;
    }

    @Override
    void editMax(boolean add) {
        max += add ? 1 : -1;
    }

    @Override
    void changeOpen(int open) {
        if ( (open >= 0) && (open <= 24) && (open <= this.close) ) {
            this.open = open;
        }
    }

    @Override
    void changeClose(int close) {
        if ( (close >= 0) && (close <= 24) && (close >= this.open) ) {
            this.close = close;
        }
    }

    @Override
    int[] specialSpaces() {
        return special;
    }

    @Override
    String config(){
        final JsonObjectBuilder CONFIG = Json.createObjectBuilder();
        CONFIG.add("max", max);
        CONFIG.add("open_from", open);
        CONFIG.add("open_to", close);
        CONFIG.add("delay", 250);
        CONFIG.add("time_shift", 5184000000L);
        CONFIG.add("simulation_speed", 800);
        return CONFIG.build().toString();
    }
}
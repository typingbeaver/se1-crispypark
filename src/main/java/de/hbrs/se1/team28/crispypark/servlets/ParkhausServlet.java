package de.hbrs.se1.team28.crispypark.servlets;

import de.hbrs.se1.team28.crispypark.*;
import de.hbrs.se1.team28.crispypark.exceptions.SpaceIsOccupiedException;
import de.hbrs.se1.team28.crispypark.stats.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */
public abstract class ParkhausServlet extends HttpServlet {

    /* abstract methods, to be defined in subclasses */
    abstract String NAME();             // each ParkhausServlet should have a name, e.g. "Level1"
    abstract int max();                 // maximum number of parking slots of a single parking level
    abstract void editMax(boolean add);
    abstract void changeOpen(int open);
    abstract void changeClose(int close);
    abstract int[] specialSpaces();     // number of spaces for DISABLED and WOMEN
    abstract String config();           // configuration of a single parking level

    static final String CURRENCY = " Euro";
    static final String TRANSACTIONS = " Parkvorgaenge";

    /**
     * HTTP GET
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        final Map<String, String[]> PARAMETER_MAP = request.getParameterMap();

        // Requests by ccm.parkhaus
        if (PARAMETER_MAP.containsKey("cmd")) {
            this.doGetCmd(request, response);
            return;
        }

        // Requests by PayServlet
        if (PARAMETER_MAP.containsKey("license")) {
            this.doGetLicense(request, response);
            return;
        }

        // Index page
        this.doGetIndex(request, response);
    }

    private void doGetCmd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        final String CMD = request.getParameter("cmd");
        System.out.println( CMD + " requested: " + request.getQueryString());
        switch ( CMD ){
            /*
             * CCM.PARKHAUS
             */
            case "config":
                out.println( config() );
                break;

            case "cars":
                final StringBuilder CARS = new StringBuilder();
                for (CarIF car: lot().getCars()) {
                    if (CARS.length() > 1)
                        CARS.append(",");
                    CARS.append(car.toString());
                }
                out.println(CARS.toString());
                break;
            case "carsArchive":
                final StringBuilder CARS_ARCHIVE = new StringBuilder();
                for (CarIF car: carsArchive()) {
                    if (CARS_ARCHIVE.length() > 1)
                        CARS_ARCHIVE.append(",");
                    CARS_ARCHIVE.append(car.toString());
                }
                out.println(CARS_ARCHIVE.toString());
                break;

            /*
             * STATISTICS
             */
            // VALUES
            // -- could be made more efficient by using additional parameters (but not possible with ccm.parkhaus)
            case "priceSumAll":
                final long PRICE_SUM_ALL = new StatsAll().getPriceSum(carsArchive());
                out.println( toCurrencyString(PRICE_SUM_ALL) + CURRENCY );
                break;
            case "priceAvgAll":
                final int PRICE_AVG_ALL = new StatsAll().getPriceAvg(carsArchive());
                out.println( toCurrencyString(PRICE_AVG_ALL) + CURRENCY );
                break;
            case "durationAvgAll":
                final int DURATION_AVG_ALL = new StatsAll().getDurationAvg(carsArchive());
                out.println( toTimeString(DURATION_AVG_ALL) );
                break;
            case "transactionsAll":
                final int TRANSACTIONS_ALL = new StatsAll().getTransctions(carsArchive());
                out.println( TRANSACTIONS_ALL + TRANSACTIONS);
                break;

            case "priceSumMonth":
                final long PRICE_SUM_MONTH = new StatsMonth(LocalDate.now()).getPriceSum(carsArchive());
                out.println( toCurrencyString(PRICE_SUM_MONTH) + CURRENCY );
                break;
            case "priceAvgMonth":
                final int PRICE_AVG_MONTH = new StatsMonth(LocalDate.now()).getPriceAvg(carsArchive());
                out.println( toCurrencyString(PRICE_AVG_MONTH) + CURRENCY );
                break;
            case "durationAvgMonth":
                final int DURATION_AVG_MONTH = new StatsMonth(LocalDate.now()).getDurationAvg(carsArchive());
                out.println( toTimeString(DURATION_AVG_MONTH) );
                break;
            case "transactionsMonth":
                final int TRANSACTIONS_MONTH = new StatsMonth(LocalDate.now()).getTransctions(carsArchive());
                out.println( TRANSACTIONS_MONTH + TRANSACTIONS);
                break;

            case "priceSumDay":
                final long PRICE_SUM_DAY = new StatsDay(LocalDate.now()).getPriceSum(carsArchive());
                out.println( toCurrencyString(PRICE_SUM_DAY) + CURRENCY );
                break;
            case "priceAvgDay":
                final int PRICE_AVG_DAY = new StatsDay(LocalDate.now()).getPriceAvg(carsArchive());
                out.println( toCurrencyString(PRICE_AVG_DAY) + CURRENCY );
                break;
            case "durationAvgDay":
                final int DURATION_AVG_DAY = new StatsDay(LocalDate.now()).getDurationAvg(carsArchive());
                out.println( toTimeString(DURATION_AVG_DAY) );
                break;
            case "transactionsDay":
                final int TRANSACTIONS_DAY = new StatsDay(LocalDate.now()).getTransctions(carsArchive());
                out.println( TRANSACTIONS_DAY + TRANSACTIONS);
                break;

            // CHARTS
            case "driverDurationChart":
                final JsonObject DRIVER_DURATION_CHART = Json.createObjectBuilder()
                        .add("data", Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                    .add("x", JSONHelper.toCarNumberArray(carsArchive()))
                                    .add("y", JSONHelper.toCarDurationArray(carsArchive()))
                                    .add("type", "bar")
                                )
                        )
                        .add("layout", Json.createObjectBuilder()
                                .add("title", "Parkdauer Kunden")
                                .add("xaxis", Json.createObjectBuilder()
                                        .add("title", "Kundennummer")
                                )
                                .add("yaxis", Json.createObjectBuilder()
                                        .add("title", "Parkdauer [Stunden h]")
                                )
                        )
                        .build();
                out.println(DRIVER_DURATION_CHART.toString());
                break;

            case "driverChart":
                final JsonObject TICKET_CHART = Json.createObjectBuilder()
                        .add("data", Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("values", JSONHelper.toCarCategoryArray(carsArchive()))
                                        .add("labels", Json.createArrayBuilder()
                                                .add("NORMAL")
                                                .add("WOMEN")
                                                .add("DISABLED")
                                        )
                                        .add("type", "pie")
                                        .add("hole", .4)
                                        .add("hoverinfo", "label+percent")
                                )
                        )
                        .add("layout", Json.createObjectBuilder()
                                .add("title", "Verteilung Fahrer-Typen")
                        )
                        .build();
                out.println(TICKET_CHART.toString());
                break;

            case "ticketChart":
                final JsonObject DRIVER_CHART = Json.createObjectBuilder()
                        .add("data", Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("values", JSONHelper.toTicketTypeArray(carsArchive()))
                                        .add("labels", Json.createArrayBuilder()
                                            .add("NORMAL")
                                            .add("FAMILY")
                                            .add("MEMBER")
                                            .add("MONTHLY")
                                        )
                                        .add("type", "pie")
                                        .add("hole", .4)
                                        .add("hoverinfo", "label+percent")
                                )
                        )
                        .add("layout", Json.createObjectBuilder()
                                .add("title", "Verteilung Ticket-Typen")
                        )
                        .build();
                out.println(DRIVER_CHART.toString());
                break;

            default:
                System.out.println("Invalid Command: " + request.getQueryString());
        }
    }

    private void doGetLicense(HttpServletRequest request, HttpServletResponse response) {
        // Get car
        final CarIF CAR = lot().getCars().stream()
                .filter(c -> c.getLicense().equals(request.getParameter("license")))
                .findFirst().orElse(null);

        // Set Attributes
        if (CAR != null) {
            request.setAttribute("license", request.getParameter("license"));
            request.setAttribute("carNo", CAR.getNo() );
            request.setAttribute("carSpace", CAR.getSpace() );

            final TicketIF TICKET = CAR.getTicket();
            request.setAttribute("ticketType", TICKET.getTicketType() );
            request.setAttribute("ticketDuration", toTimeString(TICKET.getDurationSinceEntrance()) );
            request.setAttribute( "ticketPrice", toCurrencyString(TICKET.getPrice()) );
        } else {
            request.setAttribute("error", "Kein Auto gefunden.");
        }
    }

    private void doGetIndex(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            // redirect to Index
            request.setAttribute( "spaceColoring", lot().getSpaceColoring() );
            request.setAttribute( "rateNormal", toCurrencyString(TicketType.NORMAL.getRate()) );
            request.setAttribute( "rateFamily", toCurrencyString(TicketType.FAMILY.getRate()) );
            request.setAttribute( "rateMember", toCurrencyString(TicketType.MEMBER.getRate()) );
            request.setAttribute( "rateMonthly", toCurrencyString(TicketType.MONTHLY.getRate()) );

            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Error - can't redirect index page");
            out.println("Ups, da ist was schief gelaufen. ~Server Error~");
        }
    }

    /**
     * HTTP POST
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String BODY = getBody( request );
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        System.out.println( BODY );
        final String[] PARAMS = BODY.split(",");
        /* PARAMS STRUCTURE
         * [0] event
         * [1] no                               [6] color
         * [2] entrance time (epoch time)       [7] parking space
         * [3] duration (milliseconds)          [8] TicketType/client_category
         * [4] price                            [9] DriverType/vehicle_type
         * [5] ticket id/hash                   [10] license
         */

        final String EVENT = PARAMS[0];
        switch( EVENT ){
            case "enter":
                final CarIF NEW_CAR = new Car(PARAMS);
                final int SPACE_NO = locator(NEW_CAR);
                NEW_CAR.setSpace(SPACE_NO);
                out.println(SPACE_NO + "," + NEW_CAR.getColor());
                System.out.println( "enter," + NEW_CAR );
                break;

            case "leave":
                final CarIF CAR = lot().unparkCar( Integer.parseInt(PARAMS[1]) );
                if (CAR != null) {
                    try {
                        // duration given by ccm.parkhaus
                        CAR.getTicket().leave( Integer.parseInt(PARAMS[3]) );
                    } catch (Exception e) {
                        // use real time instead
                        CAR.getTicket().leave();
                    }
                    carsArchive().add(CAR);
                    out.println(CAR.getTicket().getPrice());
                    System.out.println("Auto " + CAR.getNo() + " erfolgreich ausgefahren!");
                    System.out.println( "leave," + CAR );
                } else {
                    System.out.println("Auto " + PARAMS[1] + " wurde nicht gefunden!");
                }
                break;

            case "change_max":
                if ( this.max() < Integer.parseInt(PARAMS[2]) ) {
                    // increase
                    lot().addSpace();
                    this.editMax(true);
                } else {
                    // decrease
                    lot().removeSpace();
                    this.editMax(false);
                }
                break;

            case "change_open_from":
                this.changeOpen( Integer.parseInt(PARAMS[2]) );
                break;

            case "change_open_to":
                this.changeClose( Integer.parseInt(PARAMS[2]) );
                break;

            default:
                System.out.println( "I don't know what to do with:\n" + BODY );
        }

    }


    // auxiliary methods used in HTTP request processing

    /**
     * @return the servlet context
     */
    ServletContext getContext(){
        return getServletConfig().getServletContext();
    }

    /**
     * @return the number of the free parking lot to which the next incoming car will be directed
     */
    int locator( CarIF car ){
        // find free parking space
        DriverType carType = car.getType();
        SpaceIF space = lot().getFreeSpace(carType);
        // women can also park on normal spaces
        if (space == null && car.getType() == DriverType.WOMEN) {
            space = lot().getFreeSpace(DriverType.NORMAL);
        }
        if (space == null) {
            System.out.println("Auto " + car.getNo() + " konnte nicht geparkt werden. Kein Stellplatz verfügbar!");
            return -1;
        }

        // park car
        try {
            space.setCar( car );
            System.out.println("Auto " + car.getNo() + " wurde erfolgreich geparkt!");
            return space.getNo();
        } catch (SpaceIsOccupiedException e) {
            System.out.println("ERROR! Auto " + car.getNo() + " konnte nicht geparkt werden. Stellplatz bereits belegt!");
            return -1;
        }
    }

    /**
     * @return the list of all cars stored in the servlet context so far
     */
    @SuppressWarnings("unchecked")
    ParkingLot lot(){
        final String ATTRIBUTE = "lot";
        if ( getContext().getAttribute( ATTRIBUTE + NAME() ) == null ) {
            final ParkingLot LOT = new ParkingLot(this.max());
            LOT.setSpaceTypes( this.specialSpaces() );
            getContext().setAttribute( ATTRIBUTE + NAME(), LOT );
        }
        return (ParkingLot) getContext().getAttribute( ATTRIBUTE + NAME() );
    }

    /**
     * @return List of all fulfilled parking sessions
     */
    @SuppressWarnings("unchecked")
    List<CarIF> carsArchive(){
        final String ATTRIBUTE = "carsArchive";
        if ( getContext().getAttribute( ATTRIBUTE + NAME() ) == null ){
            getContext().setAttribute( ATTRIBUTE + NAME(), new ArrayList<Car>() );
        }
        return (List<CarIF>) getContext().getAttribute( ATTRIBUTE + NAME() );
    }

    static String toCurrencyString(long cents) {
        return String.format("%3.2f", cents / 100.0);
    }

    static String toTimeString(long duration) {
        final Duration D = Duration.of(duration, ChronoUnit.MILLIS);
        String time = "";
        if (D.toHours() > 0) {
            time = time.concat(D.toHours() + " Stunden, ");
        }
        time = time.concat(D.toMinutes()%60 + " Minuten, ");
        return time.concat(D.getSeconds()%60 + " Sekunde(n)");
    }

    /**
    * @param request the HTTP POST request
    * @return the body of the request
    */
    String getBody( HttpServletRequest request ) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = request.getInputStream();
        if (inputStream != null) {

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } else {
            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }

    @Override
    public void destroy() {
        System.out.println("Servlet destroyed.");
    }
}
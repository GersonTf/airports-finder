package com.boxever.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route {

    private String departureAirport;
    private String arrivalAirport;
    private Integer duration;

    private Route(String departure, String arrival, Integer duration) {
        this.departureAirport = departure;
        this.arrivalAirport = arrival;
        this.duration = duration;
    }

    public static List<Route> createRoutes() {
        List<Route> routes = new ArrayList<>();

        routes.add(new Route("DUB", "LHR", 1));
        routes.add(new Route("DUB", "CDG", 2));
        routes.add(new Route("CDG", "BOS", 6));
        routes.add(new Route("CDG", "BKK", 9));
        routes.add(new Route("ORD", "LAS", 2));
        routes.add(new Route("LHR", "NYC", 5));
        routes.add(new Route("NYC", "LAS", 3));
        routes.add(new Route("BOS", "LAX", 4));
        routes.add(new Route("LHR", "BKK", 9));
        routes.add(new Route("BKK", "SYD", 11));
        routes.add(new Route("LAX", "LAS", 2));
        routes.add(new Route("DUB", "ORD", 6));
        routes.add(new Route("LAX", "SYD", 13));
        routes.add(new Route("LAS", "SYD", 14));
        return routes;
    }
}

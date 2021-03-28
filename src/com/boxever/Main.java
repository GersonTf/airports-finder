package com.boxever;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        try {
            List<Route> object = findShortRoute("DUB", "SYD");

//            for (Route route : object) {
//                System.out.println(route.getDepartureAirport() + " -- " + route.getArrivalAirport() + " ( " + route.getDuration() + " )");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Route> findShortRoute(String departure, String finalDestination) throws Exception {
        //CONSIDER THAT one of the airport could not exist.
        //TODO CHECK THAT THERE IS ONE EXIT
        RoutingService routingService = new RoutingService();
        routingService.solution = new ArrayList<>();
        routingService.thereIsAnEnd = false;

        routingService.findShortRoute(departure, finalDestination);
        routingService.cleanPaths(routingService.solution, departure, finalDestination);
        return routingService.solution;
    }
}

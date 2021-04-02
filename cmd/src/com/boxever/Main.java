package com.boxever;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        try {
            FinalRouteTree result = findShortRoute("DUB", "SYD");

            for (Route route : result.getRoutes()) {
                System.out.println(route.getDepartureAirport() + " -- " + route.getArrivalAirport() + " ( " + route.getDuration() + " )");
            }
            System.out.println("time: " + result.getDuration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FinalRouteTree findShortRoute(String departure, String finalDestination) throws Exception {
        RouteService routeService = new RouteService();
        routeService.uncleanedTree = new ArrayList<>();
        routeService.thereIsAnEnd = false;

        routeService.findAllRoutes(departure, finalDestination);
        routeService.cleanPaths(routeService.uncleanedTree, departure, finalDestination);
        return routeService.finalRouteTree;
    }
}

package com.boxever;

import java.util.*;

public class RoutingService {

    static final List<Route> routes = Route.createRoutes();

    List<Route> solution = new ArrayList<>();
    Boolean thereIsAnEnd = false;
    FinalRouteTree finalRouteTree = new FinalRouteTree();

    public List<Route> findShortRoute(String departure, String finalDestination) throws Exception {
        //CONSIDER THAT one of the airport could not exist.

        for (Route route : routes) {

            if (route.getDepartureAirport().equals(departure) && route.getArrivalAirport().equals(finalDestination)) {
                thereIsAnEnd = true;
                solution.add(route);
//todo delete print
            } else if (route.getDepartureAirport().equals(departure)) {
                //todo clean the list
                solution.add(route);
                findShortRoute(route.getArrivalAirport(), finalDestination);
            }
        }

        //todo test if this works
        if (!thereIsAnEnd) {
            throw new Exception("no path found");
        }

        return solution;
    }

    //todo change name
    public void cleanPaths(List<Route> solution, String departure, String finalDestination) throws Exception {

        Map<String, List<Route>> resultMap = new HashMap<>();
        List<Route> temporaryResult = new ArrayList<>();


        //todo think carefuly if we need it
        if (!solution.stream().findFirst().get().getDepartureAirport().equals(departure)) {
            throw new Exception("paths don't start with the departure airport");
        }

        List<Route> visitedAirports = new ArrayList<>();

        int counter = 0;
        boolean isNewNode = false;

        for (Route route : solution) {
            if (counter++ > 0 && route.getDepartureAirport().equals(departure)) {
                visitedAirports.clear();
            }

            if (isNewNode && !route.getDepartureAirport().equals(departure)) {
                temporaryResult = findParentNodes(route.getDepartureAirport(), visitedAirports);
                isNewNode = false;
            }
            temporaryResult.add(route);

            if (route.getArrivalAirport().equals(finalDestination)) {
                resultMap.put(UUID.randomUUID().toString(), new ArrayList<>(temporaryResult));
                temporaryResult.clear();
                isNewNode = true;
            }

            visitedAirports.add(route);
        }

        resultMap.forEach((String key, List<Route> routeValue) -> checkValues(routeValue, key));
    }

    private List<Route> findParentNodes(String currentDeparture, List<Route> visitedAirports) throws Exception {
        List<Route> nodeWithPreviousAirports = new ArrayList<>();
        for (Route route : visitedAirports) {
            nodeWithPreviousAirports.add(route);
            if (route.getArrivalAirport().equals(currentDeparture)) {
                return nodeWithPreviousAirports;
            }
        }

        throw new Exception("one of the nodes parents is not the received departure!!!");
    }

    //todo names
    private void checkValues(List<Route> routes, String key) {
        Integer currentValue = 0;

        for (Route route : routes) {
            currentValue += route.getDuration();
        }

        if (this.finalRouteTree.getDuration() == null || this.finalRouteTree.getDuration() > currentValue) {
            this.finalRouteTree.setKey(key);
            this.finalRouteTree.setDuration(currentValue);
            this.finalRouteTree.setRoutes(routes);
        }
    }
}

package com.boxever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<Integer, List<Route>> resultMap = new HashMap<>();
        List<Route> temporaryResult = new ArrayList<>();

        //todo think carefuly if we need it
        if (!solution.stream().findFirst().get().getDepartureAirport().equals(departure)) {
            throw new Exception("paths don't start with the departure airport");
        }

        List<String> visitedAirports = new ArrayList<>();

        Integer counter = 0;

        for (Route route : solution) {


            if (visitedAirports.contains(route.getDepartureAirport())) {
                System.out.println("new node with the same parent");
            }

            if (!temporaryResult.isEmpty() && route.getDepartureAirport().equals(departure)) {
                resultMap.put(counter++, new ArrayList<>(temporaryResult));
                temporaryResult.clear();

                System.out.println("new branch from departure");
            } else {
                visitedAirports.add(route.getDepartureAirport());
            }

            temporaryResult.add(route);
        }

        resultMap.put(counter, temporaryResult);
        resultMap.forEach((Integer key, List<Route> routeValue) -> checkValues(routeValue, key));
    }

    //todo names
    private void checkValues(List<Route> routes, Integer key) {
        Integer currentValue = 0;

        for (Route route : routes) {
            currentValue += route.getDuration();
        }

        if(this.finalRouteTree.getDuration() == null || this.finalRouteTree.getDuration() > currentValue){
            this.finalRouteTree.setKey(key);
            this.finalRouteTree.setDuration(currentValue);
            this.finalRouteTree.setRoutes(routes);
        }
    }
}
